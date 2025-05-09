package com.rich.sol_bot.system.cache;

import com.rich.sol_bot.system.tool.RedisKeyGenerateTool;
import jakarta.annotation.Resource;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheService {
//
//    private static String limitString = "limit";
//    private static String cacheString = "cache";

//    public Boolean isLimit(CacheBizEnum biz, String key, Long second) {
//        String redisKey = redisKeyGenerateTool.generateName(limitString, biz.name(), key);
//        RBucket<Boolean> b = redisson.getBucket(redisKey);
//        if(b.isExists()) return true;
//        b.set(true,second, TimeUnit.SECONDS);
//        return false;
//    }

    public void cache(CacheBizEnum biz, String key, String value, Long time) {
        String redisKey = redisKeyGenerateTool.generateName(biz.name(), key);
        RBucket<String> b = redisson.getBucket(redisKey);
        b.set(value, time, TimeUnit.MILLISECONDS);
    }

    public String getCache(CacheBizEnum biz, String key) {
        String redisKey = redisKeyGenerateTool.generateName(biz.name(), key);
        RBucket<String> b = redisson.getBucket(redisKey);
        if(b.isExists()) {
            return b.get();
        } else {
            return null;
        }
    }




//    public boolean verify(CacheBizEnum biz, String key, String value) {
//        String redisKey = redisKeyGenerateTool.generateName(cacheString, biz.name(), key);
//        RBucket<String> b = redisson.getBucket(redisKey);
//        String cache = b.get();
//        if(cache != null) b.delete();
//        return value.equals(cache);
//    }

    @Resource
    private RedisKeyGenerateTool redisKeyGenerateTool;
    @Resource
    private Redisson redisson;
}
