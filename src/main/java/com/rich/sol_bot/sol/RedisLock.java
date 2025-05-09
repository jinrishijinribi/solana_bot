package com.rich.sol_bot.sol;

import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.io.Closeable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author wangqiyun
 * @since 2023/7/13 18:08
 */
public class RedisLock implements Closeable {

    private final StringRedisTemplate stringRedisTemplate;
    private final String uuid;
    private final boolean success;
    private final String key;


    public RedisLock(StringRedisTemplate stringRedisTemplate, String key) {
        this(stringRedisTemplate, key, 1L, TimeUnit.MINUTES);
    }

    public RedisLock(StringRedisTemplate stringRedisTemplate, String key, long timeout, TimeUnit timeUnit) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.uuid = UUID.randomUUID().toString();
        this.key = "RedisLock_" + key;
        BoundValueOperations<String, String> ops = stringRedisTemplate.boundValueOps(this.key);
        Boolean success = ops.setIfAbsent(uuid, timeout, timeUnit);
        this.success = (success != null && success);
    }

    public boolean _success() {
        return this.success;
    }

    public void success() {
        if (!_success())
            throw new RuntimeException("system busy");
    }

    @Override
    public void close() {
        stringRedisTemplate.execute(RedisScript.of("""
                local val = redis.call("GET", KEYS[1]);
                if val == ARGV[1]
                then
                    redis.call('DEL', KEYS[1]);
                end
                """), List.of(this.key), this.uuid);
    }
}
