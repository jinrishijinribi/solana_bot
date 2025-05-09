package com.rich.sol_bot.trade.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rich.sol_bot.pump.PumpInfoService;
import com.rich.sol_bot.pump.mapper.PumpPoolInfo;
import com.rich.sol_bot.pump.mapper.PumpPoolInfoRepository;
import com.rich.sol_bot.sol.BirdeyeService;
import com.rich.sol_bot.sol.entity.MintAccount;
import com.rich.sol_bot.sol.entity.MintUmi;
import com.rich.sol_bot.sol.entity.TokenOverview;
import com.rich.sol_bot.sol.entity.TokenSecurity;
import com.rich.sol_bot.sol.new_coin.mapper.NewCoinMapper;
import com.rich.sol_bot.sol.new_coin.mapper.NewCoinRepository;
import com.rich.sol_bot.sol.pump.PumpInfo;
import com.rich.sol_bot.sol.pump.PumpOperator;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.ThreadAsyncUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.trade.cache.TokenBalanceCacheService;
import com.rich.sol_bot.trade.cache.TokenInfoCacheService;
import com.rich.sol_bot.trade.dto.PoolInfoDTO;
import com.rich.sol_bot.trade.dto.PumpPriceDTO;
import com.rich.sol_bot.trade.dto.UriInfoDTO;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TokenBaseInfoRepository;
import com.rich.sol_bot.trade.mapper.TokenPoolInfoRepository;
import com.rich.sol_bot.trade.mapper.TradeInfoRepository;
import com.rich.sol_bot.trade.operator.SolQueryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TokenInfoService {

    @Autowired
    private PumpInfoService pumpInfoService;
    @Autowired
    private TokenPoolInfoRepository tokenPoolInfoRepository;
    @Resource
    private NewCoinMapper newCoinMapper;

    public TokenBaseInfo geneTokenBaseInfo(String address) {
        TokenBaseInfo exist = tokenInfoCacheService.getTokenBaseInfo(address);
        if(exist != null){
            return exist;
        }
        TokenBaseInfo info = tokenBaseInfoRepository.getByAddress(address);
        if(info == null) {
            info = TokenBaseInfo.builder().id(IdUtil.nextId()).address(address)
                    .createdAt(TimestampUtil.now())
                    .updatedAt(TimestampUtil.minus(1, TimeUnit.DAYS))
                    .onbusCount(0L)
                    .mintable(null)
                    .hasPool(1)
                    .supply(BigDecimal.ZERO)
                    .dropPermission(null)
                    .liquidityLock(null)
                    .build();
            info = this.buildTokenInfo(info, true);
            tokenBaseInfoRepository.saveOrUpdate(info);
        } else {
            TokenBaseInfo finalInfo = info;
            if(!info.isMain()) {
                info = this.buildTokenInfo(finalInfo, false);
            }
        }
        return info;
    }

    public TokenBaseInfo buildTokenInfo(TokenBaseInfo info, Boolean newOne) {
        try {
            if(TokenBaseInfo.mainAddress().equalsIgnoreCase(info.getAddress())){
                return info;
            }
            info.setUpdatedAt(TimestampUtil.now());
            // once
            if(BigDecimal.ZERO.compareTo(info.getSupply()) == 0
                    || info.getMintable() == null
                    || info.getDropPermission() == null
                    || info.getDecimals() == null
                    || info.getSymbol() == null
                    || info.getName() == null
                    || info.getUrl() == null) {
                MintUmi mintUmi = solQueryService.mintUmi(info.getAddress());
                Integer mintAble = StringUtils.isEmpty(mintUmi.getMint().getMintAuthority()) ? 0 : 1;
                Integer dropAuthority = StringUtils.isEmpty(mintUmi.getMint().getFreezeAuthority()) ? 1 : 0;
                info.setMintable(mintAble);
                info.setDropPermission(dropAuthority);
                info.setDecimals(mintUmi.getMint().getDecimals());
                info.setSymbol(mintUmi.getMetadata().getSymbol());
                info.setName(mintUmi.getMetadata().getName());
                info.setSupply(new BigDecimal(mintUmi.getMint().getSupply()).movePointLeft(info.getDecimals()));
                info.setUrl(mintUmi.getMetadata().getUri());
                // once
                if(info.getUrl() != null) {
                    UriInfoDTO uriInfoDTO = solQueryService.getUriInfo(info.getUrl());
                    if(uriInfoDTO != null && uriInfoDTO.getExtensions() != null) {
                        info.setTgUrl(uriInfoDTO.getExtensions().getTelegram());
                        info.setTwitterUrl(uriInfoDTO.getExtensions().getTwitter());
                    }
                    if(uriInfoDTO != null && uriInfoDTO.getTelegram() != null) {
                        info.setTgUrl(uriInfoDTO.getTelegram());
                    }
                    if(uriInfoDTO != null && uriInfoDTO.getTwitter() != null) {
                        info.setTwitterUrl(uriInfoDTO.getTwitter());
                    }
                    if(uriInfoDTO != null && uriInfoDTO.getImage() != null) {
                        info.setImage(uriInfoDTO.getImage());
                    }
                }
            }
            if(!TokenBaseInfo.mainAddress().equalsIgnoreCase(info.getAddress())){
                // 如果在pump上发现了，就等待他完成采取获取交易对
                PumpPoolInfo poolInfo = null;
                // 数据库没有ray中的池子，先从pump中获取
                if(tokenPoolInfoRepository.getByBaseMint(info.getAddress()) == null && newCoinMapper.get(info.getAddress()) == null) {
                    poolInfo = pumpInfoService.getPumpInfo(info.getAddress());
                }
                // 没有发现pump的池子，或者pump已经完成
                if(poolInfo == null || poolInfo.getComplete() == 1) {
                    PoolInfoDTO poolInfoDTO = tokenPxService.getPoolInfo(info);
                    BigDecimal px = poolInfoDTO.getPx();
                    info.setPrice(px.stripTrailingZeros().toPlainString());
                    // 计算流动性
                    if(poolInfoDTO.getSolAmount().compareTo(BigDecimal.ZERO) > 0) {
                        info.setLiquidity(poolInfoDTO.getSolAmount().stripTrailingZeros().toPlainString());
                    }
                    // 添加池子开始时间
                    if(poolInfoDTO.getPoolStartTime() != null) {
                        info.setPoolStartTime(poolInfoDTO.getPoolStartTime());
                    }
                    // 判断是否锁池
                    if(poolInfoDTO.getSupply() != null && poolInfoDTO.getLpReserve() != null) {
                        if(poolInfoDTO.getLpReserve().compareTo(poolInfoDTO.getSupply().multiply(new BigDecimal(2))) >= 0){
                            info.setLiquidityLock(1);
                        } else {
                            info.setLiquidityLock(0);
                        }
                    }
                    // 计算市值
                    info.setMkValue(info.getSupply().multiply(new BigDecimal(info.getPrice())).stripTrailingZeros().toPlainString());
                }
                // 如果是pump的交易对，重新计算
                if(info.getPoolStartTime() == null){
                    if(poolInfo != null && poolInfo.getComplete() == 0) {
                        // pump 的交易对未关闭
                        PumpPriceDTO pumpPriceDTO = tokenPxService.getPumpPx(info, poolInfo.getBondingCurve());
                        BigDecimal px = pumpPriceDTO.getPx();
                        info.setPrice(px.stripTrailingZeros().toPlainString());
                        if(pumpPriceDTO.getSolAmount().compareTo(BigDecimal.ZERO) > 0) {
                            info.setLiquidity(pumpPriceDTO.getSolAmount().movePointLeft(9).stripTrailingZeros().toPlainString());
                        }
                        info.setMkValue(info.getSupply().multiply(new BigDecimal(info.getPrice())).stripTrailingZeros().toPlainString());
                    }
                }
            }
            if(newOne) {
                return info;
            }
            BigDecimal top10Amount = tokenBalanceCacheService.top10Amount(info.getAddress());
            BigDecimal percent = top10Amount.movePointLeft(info.getDecimals()).divide(info.getSupply(),4, RoundingMode.HALF_DOWN);
            info.setTop10Percent(percent.stripTrailingZeros().toPlainString());
            info.setOnbusCount(tradeInfoRepository.countUid(info.getId()));
            tokenInfoCacheService.cacheTokenBaseInfo(info.getAddress(), info);
            tokenBaseInfoRepository.saveOrUpdate(info);
        }catch (Exception e) {
            log.error("getTokenBaseInfo", e);
        }
        return info;
    }


    public TokenBaseInfo getMain() {
        String address = TokenBaseInfo.mainAddress();
        TokenBaseInfo exist = tokenInfoCacheService.getTokenBaseInfo(address);
        if(exist != null){
            return exist;
        }
        TokenBaseInfo cacheOne = this.geneTokenBaseInfo(address);
        BigDecimal px = tokenPxService.getPx(cacheOne);
        cacheOne.setPrice(px.stripTrailingZeros().toPlainString());
        tokenBaseInfoRepository.updatePx(cacheOne.getId(), px);
        tokenInfoCacheService.cacheTokenBaseInfo(address, cacheOne, 5L, TimeUnit.MINUTES);
        return cacheOne;
    }

    public String getImage(TokenBaseInfo baseInfo) {
        if(!StringUtils.isBlank(baseInfo.getImage())) return baseInfo.getImage();
        if(StringUtils.isEmpty(baseInfo.getUrl())) return null;
        UriInfoDTO uriInfoDTO = solQueryService.getUriInfo(baseInfo.getUrl());
        if(uriInfoDTO != null && uriInfoDTO.getImage() != null) {
            tokenBaseInfoRepository.update(new LambdaUpdateWrapper<TokenBaseInfo>().set(TokenBaseInfo::getImage, uriInfoDTO.getImage())
                    .eq(TokenBaseInfo::getId, baseInfo.getId()));
            return uriInfoDTO.getImage();
        }
        return null;
    }

    @Resource
    private TokenBaseInfoRepository tokenBaseInfoRepository;
    @Resource
    private SolQueryService solQueryService;
    @Resource
    private TradeInfoRepository tradeInfoRepository;
    @Resource
    private TokenInfoCacheService tokenInfoCacheService;
    @Resource
    @Lazy
    private TokenPxService tokenPxService;
    @Resource
    private TokenBalanceCacheService tokenBalanceCacheService;
}
