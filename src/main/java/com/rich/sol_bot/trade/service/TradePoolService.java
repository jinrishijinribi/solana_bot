package com.rich.sol_bot.trade.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rich.sol_bot.bot.handler.BotSniperHandler;
import com.rich.sol_bot.pump.mapper.PumpPoolInfoRepository;
import com.rich.sol_bot.sol.entity.DexscreenerTokenPair;
import com.rich.sol_bot.sol.entity.RaydiumMint;
import com.rich.sol_bot.sol.new_coin.mapper.NewCoin;
import com.rich.sol_bot.sol.new_coin.mapper.NewCoinRepository;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.system.config.SystemConfigRepository;
import com.rich.sol_bot.trade.cache.CacheBiz;
import com.rich.sol_bot.trade.cache.CommonCacheService;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TokenBaseInfoRepository;
import com.rich.sol_bot.trade.mapper.TokenPoolInfo;
import com.rich.sol_bot.trade.mapper.TokenPoolInfoRepository;
import com.rich.sol_bot.trade.operator.SolQueryService;
import com.rich.sol_bot.wallet.UserWalletService;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TradePoolService {

    @Autowired
    private PumpPoolInfoRepository pumpPoolInfoRepository;
    @Autowired
    private TokenBaseInfoRepository tokenBaseInfoRepository;
    @Autowired
    @Lazy
    private BotSniperHandler botSniperHandler;

    /**
     * newCoin 发现的时候，并且是数据库已经有的币，则创建流动池
     */
    public void buildTokenPoolInfoFromNewCoin(NewCoin newCoin) {
        String baseMint = null;
        if(newCoin.getBaseMint().equalsIgnoreCase(TokenBaseInfo.mainAddress())){
            baseMint = newCoin.getQuoteMint();
        } else {
            baseMint = newCoin.getBaseMint();
        }
        TokenBaseInfo baseInfo = tokenBaseInfoRepository.getByAddress(baseMint);
        if(baseInfo == null) return;
        TokenPoolInfo poolInfo = TokenPoolInfo.builder().id(IdUtil.nextId()).baseMint(baseMint)
                .quoteMint(TokenBaseInfo.mainAddress()).ammKey(newCoin.getAmmKey()).poolCreateTime(TimestampUtil.now().getTime() / 1000)
                .poolOpenTime(newCoin.getPoolOpenTime())
                .build();
        tokenPoolInfoRepository.saveOrNothing(poolInfo);
        pumpPoolInfoRepository.completePumpPool(baseMint);
        // pump 发射通知所有有仓位的用户
        botSniperHandler.pumpLaunch(baseMint);
    }

    /**
     * 根据地址获取amm_key
     */
    public String getAmKey(String mintAddress) {
        String ammKey = commonCacheService.getCacheItem(0L, CacheBiz.amm_key, mintAddress);
        if(ammKey != null) return ammKey;
        TokenPoolInfo poolInfo = this.getByBaseMint(mintAddress);
        Long poolCreatedTime = 0L; // 本质是交易开始时间
        Long poolOpenTime = 0L; // 本质是交易的预设时间
        if(poolInfo != null) {
            ammKey = poolInfo.getAmmKey();
            poolCreatedTime = poolInfo.getPoolCreateTime();
            poolOpenTime = poolInfo.getPoolOpenTime();
        }
        // 获取最新的加一堆
        if(ammKey == null) {
            NewCoin newCoin = newCoinRepository.getOne(new LambdaQueryWrapper<NewCoin>()
                    .eq(NewCoin::getBaseMint, mintAddress).or().eq(NewCoin::getQuoteMint, mintAddress).last("limit 1")
            );
            if(newCoin != null && (newCoin.getQuoteMint().equalsIgnoreCase(TokenBaseInfo.mainAddress()) || newCoin.getBaseMint().equalsIgnoreCase(TokenBaseInfo.mainAddress()))){
                ammKey = newCoin.getAmmKey();
                poolOpenTime = newCoin.getPoolOpenTime();
            }
        }
        if(ammKey == null || poolCreatedTime == 0L) {
            DexscreenerTokenPair.Pairs pairs = solQueryService.pairOfRay(mintAddress);
            // 优先获取历史最大交易对
            if(pairs != null) {
                ammKey = pairs.getPairAddress();
                poolCreatedTime = pairs.getPairCreatedAt() / 1000;
                poolOpenTime = poolCreatedTime;
            }
        }
        if(poolInfo == null && ammKey != null) {
            poolInfo = TokenPoolInfo.builder().id(IdUtil.nextId()).baseMint(mintAddress)
                    .quoteMint(TokenBaseInfo.mainAddress()).ammKey(ammKey).poolCreateTime(poolCreatedTime).poolOpenTime(poolOpenTime)
                    .build();
            tokenPoolInfoRepository.saveOrNothing(poolInfo);
        }
        commonCacheService.cacheItem(0L, CacheBiz.amm_key, mintAddress, ammKey, 10L, TimeUnit.MINUTES);
        return ammKey;
    }


    /**
     * 获取poolInfo，优先获取new_coin的amm_key
     */
    public TokenPoolInfo getPoolInfo(String mintAddress) {
        TokenPoolInfo poolInfo = this.getByBaseMint(mintAddress);
        if(poolInfo != null) {
            return poolInfo;
        }
        Long poolCreatedTime = 0L; // 本质是交易开始时间
        Long poolOpenTime = 0L; // 本质是交易的预设时间
        String ammKey = null;
        // 获取最新的加一堆
        NewCoin newCoin = newCoinRepository.getOne(new LambdaQueryWrapper<NewCoin>()
                .eq(NewCoin::getBaseMint, mintAddress).or().eq(NewCoin::getQuoteMint, mintAddress).last("limit 1")
        );
        if(newCoin != null && (newCoin.getQuoteMint().equalsIgnoreCase(TokenBaseInfo.mainAddress()) || newCoin.getBaseMint().equalsIgnoreCase(TokenBaseInfo.mainAddress()))){
            ammKey = newCoin.getAmmKey();
            poolOpenTime = newCoin.getPoolOpenTime();
            poolCreatedTime = newCoin.getPoolOpenTime();
        }
        if(ammKey == null || poolCreatedTime == 0L) {
            DexscreenerTokenPair.Pairs pairs = solQueryService.pairOfRay(mintAddress);
            // 优先获取历史最大交易对
            if(pairs != null) {
                ammKey = pairs.getPairAddress();
                poolCreatedTime = pairs.getPairCreatedAt() / 1000;
                poolOpenTime = poolCreatedTime;
            }
        }
        if(ammKey != null) {
            poolInfo = TokenPoolInfo.builder().id(IdUtil.nextId()).baseMint(mintAddress)
                    .quoteMint(TokenBaseInfo.mainAddress()).ammKey(ammKey).poolCreateTime(poolCreatedTime).poolOpenTime(poolOpenTime)
                    .build();
            tokenPoolInfoRepository.saveOrNothing(poolInfo);
            return this.attachTokenAddress(poolInfo);
        }
        return poolInfo;
    }

    public TokenPoolInfo getByBaseMint(String mintAddress) {
        TokenPoolInfo poolInfo = tokenPoolInfoRepository.getByBaseMint(mintAddress);
        if(poolInfo != null) {
            if(poolInfo.getBaseTokenAddress() == null || poolInfo.getQuoteTokenAddress() == null) {
                return this.attachTokenAddress(poolInfo);
            }
            return poolInfo;
        }
        return null;
    }

    public TokenPoolInfo attachTokenAddress(TokenPoolInfo poolInfo) {
        String baseTokenAddress = null;
        String quoteTokenAddress = null;
        RaydiumMint raydiumMint = solQueryService.raydiumMint(poolInfo.getAmmKey());
        if(raydiumMint.getBaseMint().equalsIgnoreCase(TokenBaseInfo.mainAddress())){
            // base is quote
            quoteTokenAddress = raydiumMint.getBaseVault().getAssociatedTokenAddress();
            baseTokenAddress = raydiumMint.getQuoteVault().getAssociatedTokenAddress();
        } else {
            baseTokenAddress = raydiumMint.getBaseVault().getAssociatedTokenAddress();
            quoteTokenAddress = raydiumMint.getQuoteVault().getAssociatedTokenAddress();
        }
        return tokenPoolInfoRepository.buildTokenAddress(poolInfo, baseTokenAddress, quoteTokenAddress);
    }

    @Resource
    private SolQueryService solQueryService;
    @Resource
    private CommonCacheService commonCacheService;
    @Resource
    private NewCoinRepository newCoinRepository;
    @Resource
    private TokenPoolInfoRepository tokenPoolInfoRepository;
}
