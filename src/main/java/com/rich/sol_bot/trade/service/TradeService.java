package com.rich.sol_bot.trade.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rich.sol_bot.admin.stat.StatService;
import com.rich.sol_bot.bot.handler.BotDealLimitHandler;
import com.rich.sol_bot.bot.service.BotNotifyService;
import com.rich.sol_bot.limit_order.enums.OrderStatEnum;
import com.rich.sol_bot.limit_order.mapper.LimitOrder;
import com.rich.sol_bot.limit_order.mapper.LimitOrderRepository;
import com.rich.sol_bot.sniper.enums.SniperMode;
import com.rich.sol_bot.sol.BirdeyeService;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.ThreadAsyncUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.trade.enums.TradeStateEnum;
import com.rich.sol_bot.iceberg.IcebergService;
import com.rich.sol_bot.iceberg.mapper.IcebergTask;
import com.rich.sol_bot.trade.mapper.*;
import com.rich.sol_bot.trade.operator.SolQueryService;
import com.rich.sol_bot.user.UserRelationService;
import com.rich.sol_bot.user.config.mapper.UserConfig;
import com.rich.sol_bot.user.config.mapper.UserConfigRepository;
import com.rich.sol_bot.wallet.UserWalletService;
import com.rich.sol_bot.wallet.WalletBalanceStatService;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class TradeService {


    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
    @Autowired
    private StatService statService;
    @Autowired
    private UserConfigRepository userConfigRepository;

    @Transactional
    public void triggerLimitOrder(Long id) {
        LimitOrder limitOrder = limitOrderRepository.getById(id);
        if(limitOrder == null) return;
        if(limitOrder.getExpiredAt().compareTo(TimestampUtil.now()) < 0) return;
        if(!limitOrder.getState().equals(OrderStatEnum.created)) return;
        if(limitOrderRepository.submitOrder(limitOrder.getId())){
            TokenBaseInfo mainToken = tokenInfoService.getMain();
            Integer dedicated = limitOrder.getMode().equals(SniperMode.protect_mode) ? 1 : 0;
            TokenBaseInfo tokenBaseInfo = tokenBaseInfoRepository.getById(limitOrder.getTokenId());
            // 根据价格计算 min_out_amount
            switch (limitOrder.getSide()){
                case buy -> {
                    BigInteger amount = limitOrder.getAmount()
                            .multiply(new BigDecimal(mainToken.getPrice())).divide(limitOrder.getPx(), 18, RoundingMode.HALF_DOWN)
                            .multiply(BigDecimal.ONE.subtract(limitOrder.getSlippage())).movePointRight(tokenBaseInfo.getDecimals())
                            .setScale(0, RoundingMode.HALF_DOWN).toBigInteger();
                    TradeInfo info = TradeInfo.builder()
                            .id(limitOrder.getId()).uid(limitOrder.getUid())
                            .walletId(limitOrder.getWalletId())
                            .tokenInId(limitOrder.getTokenId()).tokenIn(limitOrder.getTokenName()).tokenInAmount(null)
                            .tokenOutId(mainToken.getId()).tokenOut(mainToken.getSymbol()).tokenOutAmount(limitOrder.getAmount())
                            .tx(null).gas(null).state(TradeStateEnum.created).rebate(null).platformFee(null)
                            .slippage(limitOrder.getSlippage())
                            .extraGas(limitOrder.getExtraGas())
                            .createdAt(TimestampUtil.now())
                            .dedicated(dedicated)
                            .minTokenInAmount(amount)
                            .iceId(null)
                            .build();
                    tradeInfoRepository.save(info);
                }
                case sell -> {
                    BigInteger amount = limitOrder.getAmount()
                            .multiply(limitOrder.getPx()).divide(new BigDecimal(mainToken.getPrice()), 18, RoundingMode.HALF_DOWN)
                            .multiply(BigDecimal.ONE.subtract(limitOrder.getSlippage())).movePointRight(mainToken.getDecimals())
                            .setScale(0, RoundingMode.HALF_DOWN).toBigInteger();
                    TradeInfo info = TradeInfo.builder()
                            .id(limitOrder.getId()).uid(limitOrder.getUid())
                            .walletId(limitOrder.getWalletId())
                            .tokenInId(mainToken.getId()).tokenIn(mainToken.getSymbol()).tokenInAmount(null)
                            .tokenOutId(limitOrder.getTokenId()).tokenOut(limitOrder.getTokenName()).tokenOutAmount(limitOrder.getAmount())
                            .tx(null).gas(null).state(TradeStateEnum.created).rebate(null).slippage(limitOrder.getSlippage())
                            .extraGas(limitOrder.getExtraGas())
                            .createdAt(TimestampUtil.now())
                            .minTokenInAmount(amount)
                            .dedicated(dedicated)
                            .iceId(null)
                            .build();
                    tradeInfoRepository.save(info);
                }
            }
        }
    }

//    @Transactional
    public void generateBuyTrade(Long uid, Long walletId, String tokenAddress, BigDecimal solAmount, BigDecimal extraGas, BigDecimal slippage, Integer dedicated,
                                    Long iceId
    ) {
        UserWallet wallet = userWalletRepository.ownedByUid(walletId, uid);
        TokenBaseInfo token = tokenInfoService.geneTokenBaseInfo(tokenAddress);
        TokenBaseInfo mainToken = tokenInfoService.getMain();
        if(wallet != null && token != null) {
            TradeInfo info = TradeInfo.builder()
                    .id(IdUtil.nextId()).uid(uid)
                    .walletId(walletId)
                    .tokenInId(token.getId()).tokenIn(token.getSymbol()).tokenInAmount(null)
                    .tokenOutId(mainToken.getId()).tokenOut(mainToken.getSymbol()).tokenOutAmount(solAmount)
                    .tx(null).gas(null).state(TradeStateEnum.created).rebate(null).platformFee(null)
                    .slippage(slippage)
                    .extraGas(extraGas)
                    .createdAt(TimestampUtil.now())
                    .dedicated(dedicated)
                    .iceId(iceId)
                    .build();
            tradeInfoRepository.save(info);
        }
    }

//    @Transactional
    public void generateSellTrade(Long uid, Long walletId, String tokenAddress, BigDecimal amount, BigDecimal extraGas, BigDecimal slippage, Integer dedicated,
                                     Long iceId
    ) {
        UserWallet wallet = userWalletRepository.ownedByUid(walletId, uid);
        TokenBaseInfo token = tokenInfoService.geneTokenBaseInfo(tokenAddress);
        TokenBaseInfo mainToken = tokenInfoService.getMain();
        if(wallet != null && token != null) {
            TradeInfo info = TradeInfo.builder()
                    .id(IdUtil.nextId()).uid(uid)
                    .walletId(walletId)
                    .tokenInId(mainToken.getId()).tokenIn(mainToken.getSymbol()).tokenInAmount(null)
                    .tokenOutId(token.getId()).tokenOut(token.getSymbol()).tokenOutAmount(amount)
                    .tx(null).gas(null).state(TradeStateEnum.created).rebate(null).slippage(slippage)
                    .extraGas(extraGas)
                    .createdAt(TimestampUtil.now())
                    .dedicated(dedicated)
                    .iceId(iceId)
                    .build();
            tradeInfoRepository.save(info);
        }
    }


    @Transactional
    public void tradeSuccess(Long tradeId, BigDecimal tokenInAmount, BigDecimal tokenOutAmount, BigDecimal gas, BigDecimal mainPx) {
        if(tradeInfoRepository.update(new LambdaUpdateWrapper<TradeInfo>()
                .set(TradeInfo::getState, TradeStateEnum.success)
                        .set(TradeInfo::getTokenInAmount, tokenInAmount)
                        .set(TradeInfo::getTokenOutAmount, tokenOutAmount)
                        .set(TradeInfo::getGas, gas)
                        .set(TradeInfo::getMainPx, mainPx)
                .eq(TradeInfo::getId, tradeId).eq(TradeInfo::getState, TradeStateEnum.submit)
        )){
            TradeInfo newInfo = tradeInfoRepository.getById(tradeId);
            limitOrderRepository.successOrder(tradeId);
            // 极速分佣

            // 延迟一步执行
            //todo 考虑服务重启的未执行
            scheduler.schedule(() -> {
                userRelationService.calculateTrade(newInfo);
                walletBalanceStatService.loadTrade(newInfo);
                IcebergTask task = null;
                // 处理冰山委托
                if(newInfo.getIceId() != null){
                    icebergService.successOne(newInfo.getIceId());
                    task = icebergService.getById(newInfo.getIceId());
                    botNotifyService.pushIcePartSuccess(newInfo, task);
                    if(task.getSuccessCount() + task.getFailCount() == task.getAllCount()){
                        botNotifyService.pushIceAllSuccess(newInfo);
                    }
                }
                LimitOrder limitOrder = limitOrderRepository.getById(newInfo.getId());
                if(limitOrder != null) {
                    // 处理限价委托
                    botNotifyService.pushLimitTradeSuccess(newInfo, limitOrder);
                } else {
                    // 处理常规委托
                    botNotifyService.pushNormalTradeSuccess(newInfo);
                }
                // 如果配置了自动卖出，就考虑触发自动卖出
                UserConfig userConfig = userConfigRepository.getById(newInfo.getUid());
                botDealLimitHandler.tryGenerateSellLimit(newInfo);
                statService.updateDaily(TimestampUtil.now());
                statService.updateInit();
            }, 1, TimeUnit.SECONDS);

        };
    }


    @Resource
    private TradeInfoRepository tradeInfoRepository;
    @Resource
    private TokenBaseInfoRepository tokenBaseInfoRepository;
    @Resource
    private UserWalletRepository userWalletRepository;
    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private UserRelationService userRelationService;
    @Resource
    private WalletBalanceStatService walletBalanceStatService;
    @Resource
    private BotNotifyService botNotifyService;
    @Resource
    private IcebergService icebergService;
    @Resource
    private LimitOrderRepository limitOrderRepository;
    @Resource
    @Lazy
    private BotDealLimitHandler botDealLimitHandler;
}
