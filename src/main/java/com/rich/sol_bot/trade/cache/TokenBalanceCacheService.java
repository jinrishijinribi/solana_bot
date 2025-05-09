package com.rich.sol_bot.trade.cache;

import com.rich.sol_bot.sol.entity.LargestAccounts;
import com.rich.sol_bot.system.tool.RedisKeyGenerateTool;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TradeInfo;
import com.rich.sol_bot.trade.operator.SolQueryService;
import com.rich.sol_bot.wallet.mapper.WalletBalanceStat;
import jakarta.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TokenBalanceCacheService {

    public final static String cacheSymbolPrefix = "token_balance_cache";
    public final static String cacheTop10HolderPrefix = "token10_holder_1";


    public void cacheTokenBalance(Long uid, Long walletId, Long tokenId, WalletBalanceStat amount) {
        String redisKey = redisKeyGenerateTool.generateKey(cacheSymbolPrefix, uid.toString(), walletId.toString(), tokenId.toString());
        RBucket<WalletBalanceStat> rBucket = redisson.getBucket(redisKey);
        rBucket.set(amount, 1, TimeUnit.HOURS);
    }

    public void cleanCache(Long uid, Long walletId, Long tokenId) {
        String redisKey = redisKeyGenerateTool.generateKey(cacheSymbolPrefix, uid.toString(), walletId.toString(), tokenId.toString());
        RBucket<WalletBalanceStat> rBucket = redisson.getBucket(redisKey);
        rBucket.delete();
    }

    public WalletBalanceStat getTokenBalance(Long uid, Long walletId, Long tokenId) {
        String redisKey = redisKeyGenerateTool.generateKey(cacheSymbolPrefix, uid.toString(), walletId.toString(), tokenId.toString());
        RBucket<WalletBalanceStat> rBucket = redisson.getBucket(redisKey);
        return rBucket.get();
    }

    public BigDecimal top10Amount(String address) {
        String redisKey = redisKeyGenerateTool.generateKey(cacheTop10HolderPrefix, address);
        RBucket<BigDecimal> amount = redisson.getBucket(redisKey);
        if(amount.isExists()){
            return amount.get();
        }
        LargestAccounts accounts = solQueryService.largestAccounts(address);
        List<LargestAccounts.Account> top10 = accounts.getAccounts().stream().skip(1).limit(10).toList();
        BigDecimal allAmount = top10.stream().map(LargestAccounts.Account::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        amount.set(allAmount, 15, TimeUnit.MINUTES);
        return allAmount;
    }




    @Resource
    private RedisKeyGenerateTool redisKeyGenerateTool;
    @Resource
    private RedissonClient redisson;
    @Resource
    private SolQueryService solQueryService;
}
