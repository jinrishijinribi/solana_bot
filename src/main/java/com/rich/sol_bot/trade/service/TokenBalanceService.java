package com.rich.sol_bot.trade.service;

import com.rich.sol_bot.sol.entity.SolBalance;
import com.rich.sol_bot.system.common.ThreadAsyncUtil;
import com.rich.sol_bot.trade.cache.CacheBiz;
import com.rich.sol_bot.trade.cache.CommonCacheService;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.operator.SolQueryService;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.WalletBalanceStatRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 余额
 */
@Service
public class TokenBalanceService {

    public BigDecimal getMainBalance(Long uid, String walletAddress) {
        TokenBaseInfo mainInfo = tokenInfoService.getMain();
        String key = String.join(walletAddress, ":", mainInfo.getAddress());
        String amount = commonCacheService.getCacheItem(uid, CacheBiz.token_balance, key);
        if (amount == null) {
            SolBalance solBalance = solQueryService.solBalance(walletAddress);
            amount = new BigDecimal(solBalance.getBalance()).movePointLeft(mainInfo.getDecimals()).stripTrailingZeros().toPlainString();
            commonCacheService.cacheItem(uid, CacheBiz.token_balance, key, amount);
        }
        return new BigDecimal(amount);
    }

    public BigDecimal getTokenBalance(Long uid, UserWallet wallet, TokenBaseInfo tokenBaseInfo) {
        BigDecimal amount = BigDecimal.ZERO;
        if(tokenBaseInfo.isMain()){
            amount = this.getMainBalance(uid, wallet.getAddress());
        } else {
            amount = this.getTokenBalance(uid, wallet.getAddress(), tokenBaseInfo.getAddress(), tokenBaseInfo.getDecimals());
        }
        BigDecimal finalAmount = amount;
        ThreadAsyncUtil.execAsync(() -> {
            walletBalanceStatRepository.createOrUpdate(uid, wallet.getId(), tokenBaseInfo.getId(), finalAmount);
        });
        return amount;
    }

    public BigDecimal getTokenBalance(Long uid, String walletAddress, String tokenAddress, Integer decimals) {
        String amount = commonCacheService.getCacheItem(uid, CacheBiz.token_balance, String.join(walletAddress, ":", tokenAddress));
        String key = String.join(walletAddress, ":", tokenAddress);
        if (amount == null) {
            BigDecimal am = solQueryService.tokenBalance(tokenAddress, walletAddress);
            amount = am.movePointLeft(decimals).stripTrailingZeros().toPlainString();
            commonCacheService.cacheItem(uid, CacheBiz.token_balance, key, amount);
        }
        return new BigDecimal(amount);
    }

    @Resource
    private CommonCacheService commonCacheService;
    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private SolQueryService solQueryService;
    @Resource
    private WalletBalanceStatRepository walletBalanceStatRepository;

}
