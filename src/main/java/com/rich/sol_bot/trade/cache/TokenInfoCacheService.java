package com.rich.sol_bot.trade.cache;

import com.rich.sol_bot.system.cache.CacheBizEnum;
import com.rich.sol_bot.system.tool.RedisKeyGenerateTool;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import jakarta.annotation.Resource;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenInfoCacheService {

    public void cacheTokenBaseInfo(String address, TokenBaseInfo tokenBaseInfo, Long time, TimeUnit timeUnit) {
        String redisKey = redisKeyGenerateTool.generateKey("token_cache", address);
        RBucket<TokenBaseInfo> rBucket = redisson.getBucket(redisKey);
        rBucket.set(tokenBaseInfo, time, timeUnit);
    }
    public void cacheTokenBaseInfo(String address, TokenBaseInfo tokenBaseInfo) {
        String redisKey = redisKeyGenerateTool.generateKey("token_cache", address);
        RBucket<TokenBaseInfo> rBucket = redisson.getBucket(redisKey);
        rBucket.set(tokenBaseInfo, 10, TimeUnit.SECONDS);
    }

    public TokenBaseInfo getTokenBaseInfo(String address) {
        String redisKey = redisKeyGenerateTool.generateKey("token_cache", address);
        RBucket<TokenBaseInfo> rBucket = redisson.getBucket(redisKey);
        return rBucket.get();
    }

    @Resource
    private RedisKeyGenerateTool redisKeyGenerateTool;
    @Resource
    private RedissonClient redisson;
}
