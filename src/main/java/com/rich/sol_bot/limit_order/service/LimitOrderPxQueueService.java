package com.rich.sol_bot.limit_order.service;

import com.rich.sol_bot.limit_order.dto.TokenPxDTO;
import com.rich.sol_bot.system.common.ThreadAsyncUtil;
import com.rich.sol_bot.system.tool.RedisKeyGenerateTool;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TokenBaseInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LimitOrderPxQueueService implements ApplicationRunner {

    private final ScheduledExecutorService executor;
    public static RBlockingDeque<TokenPxDTO> queue;
    private final TokenBaseInfoRepository tokenBaseInfoRepository;
    private final LimitOrderPxService limitOrderPxService;
    private final RedisKeyGenerateTool redisKeyGenerateTool;
    private final RedissonClient redissonClient;
    private final LimitOrderQueueService limitOrderQueueService;


    public LimitOrderPxQueueService(TokenBaseInfoRepository tokenBaseInfoRepository,
                                    RedisKeyGenerateTool redisKeyGenerateTool,
                                    RedissonClient redissonClient,
                                    LimitOrderQueueService limitOrderQueueService,
                                    LimitOrderPxService limitOrderPxService) {
        this.executor = Executors.newScheduledThreadPool(1);
        this.redisKeyGenerateTool = redisKeyGenerateTool;
        this.tokenBaseInfoRepository = tokenBaseInfoRepository;
        this.limitOrderPxService = limitOrderPxService;
        this.redissonClient = redissonClient;
        this.limitOrderQueueService = limitOrderQueueService;
        queue = redissonClient.getBlockingDeque(this.redisKeyGenerateTool.generateCommonKey("limit_order_px"));
    }

    public void triggerTask(String ammkey, String mint) {
        RBucket<Boolean> r = redissonClient.getBucket(this.redisKeyGenerateTool.generateCommonKey("limit_order_px_trigger_lock", ammkey, mint));
        try {
            if(r.isExists()){
                return;
            }
            r.set(true, 1 , TimeUnit.SECONDS);
            TokenBaseInfo info = tokenBaseInfoRepository.getByAddress(mint);
            TokenPxDTO tokenPxDTO = limitOrderPxService.calculateRaydiumPx(info);
            queue.add(tokenPxDTO);
        }catch (Exception e) {
            log.error("e", e);
            r.delete();
        }
    }


    public void consumeFromQueue() {
        ThreadAsyncUtil.execAsync(() -> {
            TokenPxDTO o = queue.poll();
            if(o == null) return;
            log.info("{}, {}", o.getAmmkey(), o.getPx());
            limitOrderQueueService.consumeFromQueue(o.getTokenId(), o.getPx());
        });
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        executor.scheduleWithFixedDelay(this::consumeFromQueue, 10000, 10, TimeUnit.MILLISECONDS);
    }

}
