package com.rich.sol_bot.limit_order.service;

import com.rich.sol_bot.limit_order.dto.TokenPxDTO;
import com.rich.sol_bot.sol.entity.RaydiumMint;
import com.rich.sol_bot.system.cache.CacheBizEnum;
import com.rich.sol_bot.system.cache.RedisCacheService;
import com.rich.sol_bot.system.tool.RedisKeyGenerateTool;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.operator.SolQueryService;
import com.rich.sol_bot.trade.service.TokenPxService;
import com.rich.sol_bot.trade.service.TradePoolService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class LimitOrderPxService {

    @Autowired
    private SolQueryService solQueryService;
    @Autowired
    private TokenPxService tokenPxService;

    public TokenPxDTO calculateRaydiumPx(TokenBaseInfo info) {
        String ammkey = tradePoolService.getAmKey(info.getAddress());
        String tokenAmount = this.getAmount(ammkey, info.getAddress());
        String tokenPnl = this.getPnlAmount(ammkey, info.getAddress());
        String solAmount = this.getAmount(ammkey, TokenBaseInfo.mainAddress());
        String solPnl = this.getPnlAmount(ammkey, TokenBaseInfo.mainAddress());
        if(tokenAmount == null || tokenPnl == null || solAmount == null || solPnl == null) {
            RaydiumMint raydiumMint = solQueryService.raydiumMint(ammkey);
            boolean quoteIsMain = raydiumMint.getQuoteMint().equalsIgnoreCase(TokenBaseInfo.mainAddress());
            if(tokenAmount == null) {
                tokenAmount = quoteIsMain ? raydiumMint.getBaseVault().getAmount() : raydiumMint.getQuoteVault().getAmount();
                this.cacheAmount(ammkey, info.getAddress(), tokenAmount);
            }
            if(solAmount == null) {
                solAmount = quoteIsMain ? raydiumMint.getQuoteVault().getAmount() : raydiumMint.getBaseVault().getAmount();
                this.cacheAmount(ammkey, TokenBaseInfo.mainAddress(), solAmount);
            }
            if(tokenPnl == null) {
                tokenPnl = quoteIsMain ? raydiumMint.getBasePnl() : raydiumMint.getQuotePnl();
                this.cachePnlAmount(ammkey, info.getAddress(), tokenPnl);
            }
            if(solPnl == null) {
                solPnl = quoteIsMain ? raydiumMint.getQuotePnl() : raydiumMint.getBasePnl();
                this.cachePnlAmount(ammkey, TokenBaseInfo.mainAddress(), solPnl);
            }
        }
        BigDecimal tokenSide = new BigDecimal(tokenAmount).subtract(new BigDecimal(tokenPnl)).movePointLeft(info.getDecimals());
        BigDecimal solSide = new BigDecimal(solAmount).subtract(new BigDecimal(solPnl)).movePointLeft(9);
        BigDecimal mainPx = tokenPxService.getMainPx();
        BigDecimal px = solSide.multiply(mainPx).divide(tokenSide, 18, RoundingMode.HALF_DOWN);
        return TokenPxDTO.builder().ammkey(ammkey).tokenId(info.getId()).px(px)
                .build();
    }


    public void cacheAmount(String ammkey, String mint, String amount) {
        String key = redisKeyGenerateTool.generateCommonKey(ammkey, mint).toLowerCase();
        // 60s 的缓存
        redisCacheService.cache(CacheBizEnum.limit_px_pool_amount, key, amount, 60 * 1000L);
    }

    public void cachePnlAmount(String ammkey, String mint, String amount) {
        String key = redisKeyGenerateTool.generateCommonKey(ammkey, mint).toLowerCase();
        // 60s 的缓存
        redisCacheService.cache(CacheBizEnum.limit_px_pnl_amount, key, amount, 30 * 60 * 1000L);
    }

    public String getAmount(String ammkey, String mint) {
        String key = redisKeyGenerateTool.generateKey(ammkey, mint).toLowerCase();
        return redisCacheService.getCache(CacheBizEnum.limit_px_pool_amount, key);
    }

    public String getPnlAmount(String ammkey, String mint) {
        String key = redisKeyGenerateTool.generateKey(ammkey, mint).toLowerCase();
        return redisCacheService.getCache(CacheBizEnum.limit_px_pnl_amount, key);
    }


    @Resource
    private RedisCacheService redisCacheService;
    @Resource
    private RedisKeyGenerateTool redisKeyGenerateTool;
    @Resource
    private TradePoolService tradePoolService;
}
