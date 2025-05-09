package com.rich.sol_bot.trade.service;

import com.rich.sol_bot.ex.ExchangeService;
import com.rich.sol_bot.pump.PumpInfoService;
import com.rich.sol_bot.sol.SolOperator;
import com.rich.sol_bot.sol.entity.RaydiumMint;
import com.rich.sol_bot.sol.entity.SolBalance;
import com.rich.sol_bot.system.common.ThreadAsyncUtil;
import com.rich.sol_bot.trade.cache.CacheBiz;
import com.rich.sol_bot.trade.cache.CommonCacheService;
import com.rich.sol_bot.trade.dto.PoolInfoDTO;
import com.rich.sol_bot.trade.dto.PumpPriceDTO;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.mapper.TokenBaseInfoRepository;
import com.rich.sol_bot.trade.operator.SolQueryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

@Service
public class TokenPxService {
    // 从raydium上获取价格
    public BigDecimal getPx(TokenBaseInfo baseInfo) {
        String address = baseInfo.getAddress();
        String pxs = commonCacheService.getCacheItem(0L, CacheBiz.token_px, address);
        if(pxs != null){
            return new BigDecimal(pxs);
        }
        BigDecimal px = BigDecimal.ZERO;
        if(address.equalsIgnoreCase(TokenBaseInfo.mainAddress())){
            px = calculateMain();
        } else {
            px = calculatePx(address);
        }
        if(px == null) px = BigDecimal.ZERO;
        if(px.compareTo(BigDecimal.ZERO) > 0) {
            commonCacheService.cacheItem(0L, CacheBiz.token_px, address, px.stripTrailingZeros().toPlainString(), 10L, TimeUnit.SECONDS);
            return px;
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getMainPx() {
        String pxs = commonCacheService.getCacheItem(0L, CacheBiz.token_px, TokenBaseInfo.mainAddress());
        if(pxs != null){
            return new BigDecimal(pxs);
        }
        BigDecimal px = this.calculateMain();
        if(px.compareTo(BigDecimal.ZERO) > 0) {
            commonCacheService.cacheItem(0L, CacheBiz.token_px, TokenBaseInfo.mainAddress(), px.stripTrailingZeros().toPlainString(), 60L, TimeUnit.SECONDS);
            return px;
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal calculateMain() {
        BigDecimal px = exchangeService.getPx();
        if(px == null) return new BigDecimal("180");
        return px;
    }

    private BigDecimal calculatePx(String address) {
        TokenBaseInfo baseInfo = tokenInfoService.geneTokenBaseInfo(address);
        BigDecimal px = BigDecimal.ZERO;
        String ammKey = tradePoolService.getAmKey(address);
        RaydiumMint raydiumMint = solQueryService.raydiumMint(ammKey);
        BigDecimal baseMint = BigDecimal.ONE;
        BigDecimal quoteMint = BigDecimal.ONE;
        if(raydiumMint != null && raydiumMint.getBaseMint() != null && raydiumMint.getQuoteMint() != null) {
            TokenBaseInfo tokenMain = tokenInfoService.getMain();
            if(TokenBaseInfo.mainAddress().equalsIgnoreCase(raydiumMint.getQuoteMint())){
                baseMint = new BigDecimal(raydiumMint.getBaseVault().getAmount()).subtract(new BigDecimal(raydiumMint.getBasePnl())).movePointLeft(baseInfo.getDecimals());
                quoteMint = new BigDecimal(raydiumMint.getQuoteVault().getAmount()).subtract(new BigDecimal(raydiumMint.getQuotePnl())).movePointLeft(tokenMain.getDecimals());
            } else {
                baseMint = new BigDecimal(raydiumMint.getQuoteVault().getAmount()).subtract(new BigDecimal(raydiumMint.getQuotePnl())).movePointLeft(baseInfo.getDecimals());
                quoteMint = new BigDecimal(raydiumMint.getBaseVault().getAmount()).subtract(new BigDecimal(raydiumMint.getBasePnl())).movePointLeft(tokenMain.getDecimals());;
            }
            px = quoteMint.multiply(new BigDecimal(tokenMain.getPrice())).divide(baseMint, 18, RoundingMode.HALF_DOWN);
        }
        return px;
    }

    public PoolInfoDTO getPoolInfo(TokenBaseInfo baseInfo) {
        return this.getPoolInfoRay(baseInfo);
    }

    public PoolInfoDTO getPoolInfoRay(TokenBaseInfo baseInfo) {
        String address = baseInfo.getAddress();
        String pxs = commonCacheService.getCacheItem(0L, CacheBiz.token_px, address);
        BigDecimal px = BigDecimal.ZERO;
        BigDecimal solAmount = BigDecimal.ZERO;
        if(pxs != null){
            px = new BigDecimal(pxs);
            return PoolInfoDTO.builder().px(px).solAmount(solAmount).poolStartTime(null).build();
        }
        String ammKey = tradePoolService.getAmKey(address);
        RaydiumMint raydiumMint = solQueryService.raydiumMint(ammKey);
        BigDecimal baseMint = BigDecimal.ONE;
        BigDecimal quoteMint = BigDecimal.ONE;
        Long poolStartTime = null;
        if(raydiumMint != null && raydiumMint.getBaseMint() != null && raydiumMint.getQuoteMint() != null && raydiumMint.getPoolOpenTime() != null) {
            TokenBaseInfo tokenMain = tokenInfoService.getMain();
            poolStartTime = Long.valueOf(raydiumMint.getPoolOpenTime());
            if(TokenBaseInfo.mainAddress().equalsIgnoreCase(raydiumMint.getQuoteMint())){
                baseMint = new BigDecimal(raydiumMint.getBaseVault().getAmount()).subtract(new BigDecimal(raydiumMint.getBasePnl())).movePointLeft(baseInfo.getDecimals());
                quoteMint = new BigDecimal(raydiumMint.getQuoteVault().getAmount()).subtract(new BigDecimal(raydiumMint.getQuotePnl())).movePointLeft(tokenMain.getDecimals());
                solAmount = quoteMint;
            } else {
                baseMint = new BigDecimal(raydiumMint.getQuoteVault().getAmount()).subtract(new BigDecimal(raydiumMint.getQuotePnl())).movePointLeft(baseInfo.getDecimals());;
                quoteMint = new BigDecimal(raydiumMint.getBaseVault().getAmount()).subtract(new BigDecimal(raydiumMint.getBasePnl())).movePointLeft(tokenMain.getDecimals());;
                solAmount = quoteMint;
            }
            px = quoteMint.multiply(new BigDecimal(tokenMain.getPrice())).divide(baseMint, 18, RoundingMode.HALF_DOWN);
            commonCacheService.cacheItem(0L, CacheBiz.token_px, address, px.stripTrailingZeros().toPlainString(), 10L, TimeUnit.SECONDS);
            return PoolInfoDTO.builder().px(px)
                    .solAmount(solAmount).supply(new BigDecimal(raydiumMint.getLpMint().getSupply()))
                    .lpReserve(raydiumMint.getLpReserve())
                    .poolStartTime(poolStartTime).ammKey(ammKey).build();
        }
        return PoolInfoDTO.builder().px(px).solAmount(solAmount).poolStartTime(poolStartTime).ammKey(ammKey).build();
    }


    // (30 + sol数量) * sol价格 / (73000191 + 代币数量)
    public PumpPriceDTO getPumpPx(TokenBaseInfo baseInfo,  String bondingCurve) {
        String mint = baseInfo.getAddress();
        String pxs = commonCacheService.getCacheItem(0L, CacheBiz.token_px, mint);
        if(pxs != null){
            return PumpPriceDTO.builder().px(new BigDecimal(pxs)).solAmount(BigDecimal.ZERO).complete(false).build();
        }
        TokenBaseInfo tokenMain = tokenInfoService.getMain();

        boolean complete = false;
        SolBalance s = solQueryService.solBalance(bondingCurve);
        BigDecimal tokenBal = solQueryService.tokenBalance(mint, bondingCurve);
        BigDecimal solBal = new BigDecimal(s.getBalance());
        if(tokenBal.compareTo(new BigDecimal("0.1").movePointRight(9 + baseInfo.getDecimals())) < 0) {
            complete = true;
            pumpInfoService.pumpComplete(baseInfo.getAddress());
        }
        BigDecimal px = new BigDecimal(30).add(solBal.movePointLeft(9)).multiply(new BigDecimal(tokenMain.getPrice()))
                .divide(new BigDecimal(73000191).add(tokenBal.movePointLeft(baseInfo.getDecimals())), 10, RoundingMode.HALF_DOWN);
        if(px.compareTo(BigDecimal.ZERO) > 0) {
            commonCacheService.cacheItem(0L, CacheBiz.token_px, mint, px.stripTrailingZeros().toPlainString(), 10L, TimeUnit.SECONDS);
            return PumpPriceDTO.builder().px(px).complete(complete).solAmount(solBal).build();
        }
        return PumpPriceDTO.builder().px(BigDecimal.ZERO).complete(complete).solAmount(solBal).build();
    }


    public BigDecimal getPumpPercent(String mint, String bondingCurve) {
        String percent = commonCacheService.getCacheItem(0L, CacheBiz.pump_percent, mint);
        if(percent != null){
            return new BigDecimal(percent);
        }
        BigDecimal tokenBal = solQueryService.tokenBalance(mint, bondingCurve);
        TokenBaseInfo baseInfo = tokenInfoService.geneTokenBaseInfo(mint);
        BigDecimal tokenPercent = new BigDecimal("1")
                .subtract(tokenBal.movePointLeft(9 + baseInfo.getDecimals()))
                .divide(new BigDecimal("0.8"), 4, RoundingMode.HALF_DOWN);
        commonCacheService.cacheItem(0L, CacheBiz.pump_percent, mint, tokenPercent.stripTrailingZeros().toPlainString(), 10L, TimeUnit.SECONDS);
        return tokenPercent;
    }

    @Resource
    private CommonCacheService commonCacheService;
    @Resource
    private SolQueryService solQueryService;
    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private ExchangeService exchangeService;
    @Resource
    private TradePoolService tradePoolService;
    @Resource
    private PumpInfoService pumpInfoService;
}
