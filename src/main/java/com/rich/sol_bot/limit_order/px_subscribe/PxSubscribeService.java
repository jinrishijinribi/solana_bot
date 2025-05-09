package com.rich.sol_bot.limit_order.px_subscribe;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rich.sol_bot.limit_order.px_subscribe.mapper.AccountSubscribe;
import com.rich.sol_bot.limit_order.px_subscribe.mapper.AccountSubscribeLive;
import com.rich.sol_bot.limit_order.px_subscribe.mapper.AccountSubscribeLiveRepository;
import com.rich.sol_bot.limit_order.px_subscribe.mapper.AccountSubscribeRepository;
import com.rich.sol_bot.sol.SolOperator;
import com.rich.sol_bot.sol.SolPxOperator;
import com.rich.sol_bot.sol.entity.RaydiumMint;
import com.rich.sol_bot.sol.entity.SolPxSubscribeResult;
import com.rich.sol_bot.sol.queue.SolQueueService;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.system.tool.RedisKeyGenerateTool;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TokenPoolInfo;
import com.rich.sol_bot.trade.mapper.TokenPoolInfoRepository;
import com.rich.sol_bot.trade.operator.SolQueryService;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.trade.service.TradePoolService;
import jakarta.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class PxSubscribeService {

    @Autowired
    private TokenInfoService tokenInfoService;
    @Autowired
    private TradePoolService tradePoolService;

    /**
     * 添加需要监听的池子
     * @param mint
     * @return
     */
    public void generateAccountSubscribe(String mint) {
        TokenPoolInfo poolInfo = tradePoolService.getPoolInfo(mint);
        if(poolInfo == null) return;
        String ammkey = poolInfo.getAmmKey();
        TokenBaseInfo baseInfo = tokenInfoService.geneTokenBaseInfo(mint);
        RaydiumMint raydiumMint = solQueryService.raydiumMint(ammkey);
        BigInteger basePnl = new BigInteger(raydiumMint.getBasePnl());
        BigInteger quotePnl = new BigInteger(raydiumMint.getQuotePnl());
        String baseMint = raydiumMint.getBaseMint();
        String quoteMint = raydiumMint.getQuoteMint();
        AccountSubscribe accountSubscribe = accountSubscribeRepository.generateOrUpdate(baseInfo.getId(), ammkey, baseMint, quoteMint, basePnl, quotePnl);
        accountSubscribeLiveRepository.generateAccountSubscribeLive(ammkey, accountSubscribe.getId(), baseMint,
                Long.valueOf(raydiumMint.getBaseVault().getAmount()),
                raydiumMint.getBaseVault().getAssociatedTokenAddress());
        accountSubscribeLiveRepository.generateAccountSubscribeLive(ammkey, accountSubscribe.getId(), quoteMint,
                Long.valueOf(raydiumMint.getQuoteVault().getAmount()),
                raydiumMint.getQuoteVault().getAssociatedTokenAddress());
    }

    /**
     * 给live 续时
     */
    public void accountSubscribeMoreLive(Long tokenId) {
        AccountSubscribe accountSubscribe = accountSubscribeRepository.getOne(new LambdaQueryWrapper<AccountSubscribe>().eq(AccountSubscribe::getTokenId, tokenId).last("limit 1"));
        if(accountSubscribe == null) {
            return;
        }
        List<AccountSubscribeLive> list = accountSubscribeLiveRepository.list(new LambdaQueryWrapper<AccountSubscribeLive>()
                .eq(AccountSubscribeLive::getAccountSubscribeId, accountSubscribe.getId())
        );
        for(AccountSubscribeLive live: list) {
            accountSubscribeLiveRepository.update(new LambdaUpdateWrapper<AccountSubscribeLive>()
                            .set(AccountSubscribeLive::getLiveUntil, TimestampUtil.plus(1, TimeUnit.HOURS))
                    .eq(AccountSubscribeLive::getId, live.getId()));
        }
    }

    /**
     * 重连
     * @param subscribeLive
     */
    public void connectToLive(AccountSubscribeLive subscribeLive) {
        SolPxSubscribeResult.SubscribeItem result = solPxOperator.reSubscribe(subscribeLive.getAmmkey(), subscribeLive.getMint(), subscribeLive.getAddress());
        if(result == null) return;
        accountSubscribeLiveRepository.setSubmitId(subscribeLive.getId(), result.getSubmitId());
    }

    /**
     * 判断是否已经断连
     */
    public boolean needReConnect(AccountSubscribeLive live) {
        // 1min 内校验过的，不用检查
        if(live.getLastFailAt() != null && live.getLastFailAt().compareTo(TimestampUtil.minus(1, TimeUnit.MINUTES)) > 0) return false;
        RaydiumMint raydiumMint = solQueryService.raydiumMint(live.getAmmkey());
        String baseMint = raydiumMint.getBaseMint();
        if(baseMint.equalsIgnoreCase(live.getMint())){
            if(!Long.valueOf(raydiumMint.getBaseVault().getAmount()).equals(live.getAmount())){
                accountSubscribeLiveRepository.finishCheck(live.getId(), Long.valueOf(raydiumMint.getBaseVault().getAmount()), live.getCheckFailCount() + 1);
            }
        } else {
            if(!Long.valueOf(raydiumMint.getQuoteVault().getAmount()).equals(live.getAmount())){
                accountSubscribeLiveRepository.finishCheck(live.getId(), Long.valueOf(raydiumMint.getQuoteVault().getAmount()), live.getCheckFailCount() + 1);
            }
        }
        live = accountSubscribeLiveRepository.getById(live.getId());
        // 三次校验失败的重连
        return live.getCheckFailCount() > 3;
    }

    /**
     * 每10s更新一次数据库
     */
    public void updateAmount(String ammkey, String mint, Long amount) {
        if(!lockKey(ammkey + mint)) return;
        accountSubscribeLiveRepository.update(new LambdaUpdateWrapper<AccountSubscribeLive>()
                        .set(AccountSubscribeLive::getAmount, amount)
                        .set(AccountSubscribeLive::getCheckFailCount, 0)
                        .set(AccountSubscribeLive::getLastFailAt, null)
                        .set(AccountSubscribeLive::getUpdatedAt, TimestampUtil.now())
                .eq(AccountSubscribeLive::getAmmkey, ammkey).eq(AccountSubscribeLive::getMint, mint));
    }

    /**
     * 取消订阅
     */
    public void unsubscribe(AccountSubscribeLive live) {
        if(live.getSubmitId() != null) {
            if(solPxOperator.unsubscribe(live.getSubmitId(), live.getAddress())) {
                accountSubscribeLiveRepository.update(new LambdaUpdateWrapper<AccountSubscribeLive>()
                                .set(AccountSubscribeLive::getSubmitId, null).set(AccountSubscribeLive::getCheckFailCount, 0)
                        .eq(AccountSubscribeLive::getId, live.getId()));
            };
        }
    }



    public boolean lockKey(String type) {
        RBucket<Boolean> lock = redissonClient.getBucket(generateLockKey(type));
        return lock.setIfAbsent(true, Duration.ofSeconds(10));
    }

    public String generateLockKey(String type) {
        return redisKeyGenerateTool.generateName("PxSubscribeService", type);
    };


    @Resource
    private AccountSubscribeRepository accountSubscribeRepository;
    @Resource
    private AccountSubscribeLiveRepository accountSubscribeLiveRepository;
    @Resource
    private TokenPoolInfoRepository tokenPoolInfoRepository;
    @Resource
    private SolPxOperator solPxOperator;
    @Resource
    private SolQueryService solQueryService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RedisKeyGenerateTool redisKeyGenerateTool;


}
