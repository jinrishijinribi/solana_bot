package com.rich.sol_bot.limit_order.px_subscribe;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rich.sol_bot.limit_order.mapper.LimitOrder;
import com.rich.sol_bot.limit_order.mapper.LimitOrderMapper;
import com.rich.sol_bot.limit_order.px_subscribe.mapper.AccountSubscribe;
import com.rich.sol_bot.limit_order.px_subscribe.mapper.AccountSubscribeLive;
import com.rich.sol_bot.limit_order.px_subscribe.mapper.AccountSubscribeLiveRepository;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.system.tool.RedisKeyGenerateTool;
import jakarta.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class PxSubscribeSchedule implements ApplicationRunner {

    @Autowired
    private LimitOrderMapper limitOrderMapper;

    public String generateLockKey(String type) {
        return redisKeyGenerateTool.generateName("PxSubscribeSchedule", type);
    };

    @Scheduled(initialDelay = 10 * 1000, fixedDelay = 60 * 1000)
    public void firstConnect() {
        if(lockKey("firstConnect")) {
            List<AccountSubscribeLive> list = accountSubscribeLiveRepository.list(new LambdaQueryWrapper<AccountSubscribeLive>()
                            .isNull(AccountSubscribeLive::getSubmitId)
                    .gt(AccountSubscribeLive::getLiveUntil, TimestampUtil.now()));
            for(AccountSubscribeLive live: list) {
                pxSubscribeService.connectToLive(live);
            }
        }
        unlock("firstConnect");
    }

    @Scheduled(initialDelay = 10 * 1000, fixedDelay = 60 * 1000)
    public void retryConnect() {
        if(lockKey("retryConnect")) {
            // 1min 没更新的连接的数据
            List<AccountSubscribeLive> list = accountSubscribeLiveRepository.list(new LambdaQueryWrapper<AccountSubscribeLive>()
                    .isNotNull(AccountSubscribeLive::getSubmitId).le(AccountSubscribeLive::getUpdatedAt, TimestampUtil.minus(1, TimeUnit.MINUTES))
                    .gt(AccountSubscribeLive::getLiveUntil, TimestampUtil.now()));
            for(AccountSubscribeLive live: list) {
                if(pxSubscribeService.needReConnect(live)) {
                    pxSubscribeService.connectToLive(live);
                }
            }
        }
        unlock("retryConnect");
    }

    // 每10min，发起一次续费
    @Scheduled(initialDelay = 10 * 1000, fixedDelay = 10 * 60 * 1000)
    public void moreLive() {
        if(lockKey("moreLive")) {
            List<Long> tokenIds = limitOrderMapper.listTokenIds(TimestampUtil.now());
            for(Long tokenId: tokenIds) {
                pxSubscribeService.accountSubscribeMoreLive(tokenId);
            }
        }
        unlock("moreLive");
    }

    // 每10min，发起一次取消订阅
    @Scheduled(initialDelay = 10 * 1000, fixedDelay = 10 * 60 * 1000)
    public void unsubscribe() {
        if(lockKey("unsubscribe")) {
            List<AccountSubscribeLive> list = accountSubscribeLiveRepository.list(new LambdaQueryWrapper<AccountSubscribeLive>()
                    .isNotNull(AccountSubscribeLive::getSubmitId)
                    .le(AccountSubscribeLive::getLiveUntil, TimestampUtil.now()));
            for(AccountSubscribeLive i: list) {
                pxSubscribeService.unsubscribe(i);
            }
        }
        unlock("unsubscribe");
    }

    // 重制submit id
    public void cleanSubmitId() {
        accountSubscribeLiveRepository.update(new LambdaUpdateWrapper<AccountSubscribeLive>()
                .set(AccountSubscribeLive::getSubmitId,null).isNotNull(AccountSubscribeLive::getSubmitId));
    }


    public boolean lockKey(String type) {
        RBucket<Boolean> lock = redissonClient.getBucket(generateLockKey(type));
        return lock.setIfAbsent(true, Duration.ofSeconds(60));
    }

    public void unlock(String type) {
        RBucket<Boolean> lock = redissonClient.getBucket(generateLockKey(type));
        lock.delete();
    }

    @Resource
    private RedisKeyGenerateTool redisKeyGenerateTool;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private AccountSubscribeLiveRepository accountSubscribeLiveRepository;
    @Resource
    private PxSubscribeService pxSubscribeService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.cleanSubmitId();
    }
}
