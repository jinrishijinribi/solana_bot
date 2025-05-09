package com.rich.sol_bot.sol;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.gson.Gson;
import com.rich.sol_bot.sniper.mapper.SniperPlanTx;
import com.rich.sol_bot.sniper.mapper.SniperPlanTxMapper;
import com.rich.sol_bot.sol.queue.Message;
import com.rich.sol_bot.sol.queue.SolQueueService;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.system.tool.RedisKeyGenerateTool;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wangqiyun
 * @since 2024/3/26 17:57
 */

@Service
@Slf4j
public class RouteScanTask {
    @Autowired
    private RouteScanService routeScanService;

//    @PostConstruct
//    public void init() {
//        scheduledExecutorService.scheduleWithFixedDelay(() -> {
//            try (RedisLock redisLock = new RedisLock(stringRedisTemplate, "RouteScanTask_task")) {
//                if (!redisLock._success()) return;
//                List<SniperPlanTx> sniperPlanTxList = sniperPlanTxMapper.selectList(new LambdaQueryWrapper<SniperPlanTx>().le(SniperPlanTx::getPoolStartTime, System.currentTimeMillis() / 1000L).eq(SniperPlanTx::getSuccess, 0));
//                for (SniperPlanTx sniperPlanTx : sniperPlanTxList) {
//                    solQueueService.send(sniperPlanTx);
//                    sniperPlanTxMapper.update(null, new LambdaUpdateWrapper<SniperPlanTx>().set(SniperPlanTx::getSuccess, 1).eq(SniperPlanTx::getId, sniperPlanTx.getId()));
//                }
//            } catch (Exception e) {
//                log.info("RouteScanTask: {}", e.toString());
//            }
//        }, 0, 100, TimeUnit.MILLISECONDS);
//    }


    @PostConstruct
    public void init() {
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            String key = redisKeyGenerateTool.generateKey("RouteScanTask_task");
            try (RedisLock redisLock = new RedisLock(stringRedisTemplate, key)) {
                if (!redisLock._success()) return;
                BoundZSetOperations<String, String> ops = stringRedisTemplate.boundZSetOps(routeScanService.TX_QUEUE());
                String id;
                while (true) {
                    id = null;
                    Set<String> range = ops.range(0, 0);
                    if (range != null && !range.isEmpty()) {
                        id = range.iterator().next();
                    }
                    if (StringUtils.hasLength(id)) {
                        Double score = ops.score(id);
                        if (score == null) break;
//                        log.info("{}, {}", score.longValue(), TimestampUtil.now().getTime()/1000);
                        if (score.longValue() > (TimestampUtil.now().getTime() / 1000)) {
                            break;
                        }
                        ops.remove(id);
                        String msg = stringRedisTemplate.opsForValue().get(routeScanService.TX_INFO() + id);
                        if (StringUtils.hasLength(msg))
                            solQueueService.send(new Gson().fromJson(msg, Message.class));
                    } else {
                        break;
                    }
                }
            } catch (Exception e) {
                log.info("RouteScanTask: {}", e.toString());
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }


    @Resource
    private SolQueueService solQueueService;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    @Resource
    private SniperPlanTxMapper sniperPlanTxMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisKeyGenerateTool redisKeyGenerateTool;

}
