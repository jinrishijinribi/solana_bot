package com.rich.sol_bot.bot.handler;

import com.rich.sol_bot.bot.handler.enums.LockStateEnum;
import com.rich.sol_bot.system.tool.RedisKeyGenerateTool;
import jakarta.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class BotStateService {

    /**
     * 避免重复操作
     * @param chatId
     * @param content
     * @return false 表示重复操作
     */
    public boolean lockAction(Long chatId, String content) {
        RBucket<String> rBucket = redissonClient.getBucket(redisKeyGenerateTool.generateName(chatId.toString() + "a"));
        String val = rBucket.get();
        if(val == null){
            rBucket.set(content, 5, TimeUnit.SECONDS);
            return true;
        };
        if(!val.equals(content)){
            rBucket.set(content, 5, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    public boolean isLockState(Long chatId) {
        RBucket<String> rBucket = redissonClient.getBucket(redisKeyGenerateTool.generateName(chatId.toString()));
        String val = rBucket.get();
        return val != null;
    }

    public LockStateEnum getState(Long chatId) {
        RBucket<LockStateEnum> rBucket = redissonClient.getBucket(redisKeyGenerateTool.generateName(chatId.toString()));
        return rBucket.get();
    }

    public void lockState(Long chatId, LockStateEnum type) {
        RBucket<LockStateEnum> rBucket = redissonClient.getBucket(redisKeyGenerateTool.generateName(chatId.toString()));
        rBucket.set(type, 24, TimeUnit.HOURS);
    }

    public void unlockState(Long chatId) {
        RBucket<LockStateEnum> rBucket = redissonClient.getBucket(redisKeyGenerateTool.generateName(chatId.toString()));
        LockStateEnum val = rBucket.get();
        if(val != null){
            rBucket.delete();
        }
    }


    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RedisKeyGenerateTool redisKeyGenerateTool;



}
