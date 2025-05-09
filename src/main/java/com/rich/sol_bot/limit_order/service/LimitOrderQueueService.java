package com.rich.sol_bot.limit_order.service;

import com.rich.sol_bot.limit_order.mapper.LimitOrder;
import com.rich.sol_bot.limit_order.px_subscribe.PxSubscribeService;
import com.rich.sol_bot.system.tool.RedisKeyGenerateTool;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TokenBaseInfoRepository;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.trade.service.TradeService;
import jakarta.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Set;

@Service
public class LimitOrderQueueService {

    private static final Logger log = LoggerFactory.getLogger(LimitOrderQueueService.class);
    public static String queuePrefix = "limit_order_queue";
    public static String queuePrefixBig = "big";
    public static String queuePrefixSmall = "small";
    @Autowired
    private TokenBaseInfoRepository tokenBaseInfoRepository;

    public void submitToQueue(LimitOrder limitOrder) {
        if(limitOrder.getPx().compareTo(limitOrder.getNowPx()) > 0) {
            String queueName = redisKeyGenerateTool.generateCommonKey(queuePrefix, limitOrder.getTokenId().toString(), queuePrefixBig);
            BoundZSetOperations<String, String> ops = stringRedisTemplate.boundZSetOps(queueName);
            ops.add(limitOrder.getId().toString(), limitOrder.getPx().doubleValue());
        }
        if(limitOrder.getPx().compareTo(limitOrder.getNowPx()) < 0) {
            String queueName = redisKeyGenerateTool.generateCommonKey(queuePrefix, limitOrder.getTokenId().toString(), queuePrefixSmall);
            BoundZSetOperations<String, String> ops = stringRedisTemplate.boundZSetOps(queueName);
            ops.add(limitOrder.getId().toString(), limitOrder.getPx().negate().doubleValue());
        }
        TokenBaseInfo baseInfo = tokenBaseInfoRepository.getById(limitOrder.getTokenId());
        pxSubscribeService.generateAccountSubscribe(baseInfo.getAddress());
    }


    public void consumeFromQueue(Long tokenId, BigDecimal px) {
        // 处理大于价格队列
        this.consumeBigQueue(tokenId, px);
        // 处理小于价格队列
        this.consumeSmallQueue(tokenId,px);
    }

    public void consumeBigQueue(Long tokenId, BigDecimal px) {
        if(!lockKey("consumeBigQueue")) return;
        String bigQueue = redisKeyGenerateTool.generateCommonKey(queuePrefix, tokenId.toString(), queuePrefixBig);
        BoundZSetOperations<String, String> bigQueueOps = stringRedisTemplate.boundZSetOps(bigQueue);
        while (true){
            Set<String> bigRange = bigQueueOps.range(0, 0);
            if (bigRange != null && !bigRange.isEmpty()) {
                String id = bigRange.iterator().next();
                Double score = bigQueueOps.score(id);
                // 向上越过
                if(score != null && new BigDecimal(score).compareTo(px) < 0) {
                    bigQueueOps.remove(id);
                    log.info("开始下限价单:{}", id);
                    tradeService.triggerLimitOrder(Long.valueOf(id));
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        unlock("consumeBigQueue");
    }

    public void consumeSmallQueue(Long tokenId, BigDecimal px) {
        if(!lockKey("consumeSmallQueue")) return;
        String smallQueue = redisKeyGenerateTool.generateCommonKey(queuePrefix, tokenId.toString(), queuePrefixSmall);
        BoundZSetOperations<String, String> smallQueueOps = stringRedisTemplate.boundZSetOps(smallQueue);
        while (true) {
            Set<String> smallRange = smallQueueOps.range(0, 0);
            if (smallRange != null && !smallRange.isEmpty()) {
                String id = smallRange.iterator().next();
                Double score = smallQueueOps.score(id);
                if(score != null && new BigDecimal(score).negate().compareTo(px) > 0) {
                    smallQueueOps.remove(id);
                    log.info("开始下限价单:{}", id);
                    tradeService.triggerLimitOrder(Long.valueOf(id));
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        unlock("consumeSmallQueue");
    }

    public boolean lockKey(String type) {
        RBucket<Boolean> lock = redissonClient.getBucket(generateLockKey(type));
        return lock.setIfAbsent(true, Duration.ofSeconds(60));
    }

    public void unlock(String type) {
        RBucket<Boolean> lock = redissonClient.getBucket(generateLockKey(type));
        lock.delete();
    }

    public String generateLockKey(String type) {
        return redisKeyGenerateTool.generateName("LimitOrderQueueService", type);
    };

    @Resource
    private RedisKeyGenerateTool redisKeyGenerateTool;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private TradeService tradeService;
    @Resource
    private PxSubscribeService pxSubscribeService;
    @Resource
    private RedissonClient redissonClient;
}
