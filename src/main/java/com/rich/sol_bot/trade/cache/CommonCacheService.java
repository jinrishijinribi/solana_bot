package com.rich.sol_bot.trade.cache;

import com.rich.sol_bot.system.tool.RedisKeyGenerateTool;
import com.rich.sol_bot.wallet.mapper.WalletBalanceStat;
import jakarta.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CommonCacheService {

    public void cacheItem(Long uid, CacheBiz biz,  String key, String item) {
        String redisKey = redisKeyGenerateTool.generateKey(uid.toString(), biz.getValue(), key);
        RBucket<String> rBucket = redisson.getBucket(redisKey);
        rBucket.set(item, 10, TimeUnit.SECONDS);
    }

    public void cacheItem(Long uid, CacheBiz biz,  String key, String item, Long t, TimeUnit tu) {
        String redisKey = redisKeyGenerateTool.generateKey(uid.toString(), biz.getValue(), key);
        RBucket<String> rBucket = redisson.getBucket(redisKey);
        rBucket.set(item, t, tu);
    }

    public void cleanCache(Long uid, CacheBiz biz,  String key) {
        String redisKey = redisKeyGenerateTool.generateKey(uid.toString(), biz.getValue(), key);
        RBucket<String> rBucket = redisson.getBucket(redisKey);
        rBucket.delete();
    }

    public String getCacheItem(Long uid, CacheBiz biz,  String key) {
        String redisKey = redisKeyGenerateTool.generateKey(uid.toString(), biz.getValue(), key);
        RBucket<String> rBucket = redisson.getBucket(redisKey);
        return rBucket.get();
    }

    @Resource
    private RedisKeyGenerateTool redisKeyGenerateTool;
    @Resource
    private RedissonClient redisson;
}
