package com.rich.sol_bot.sniper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rich.sol_bot.sniper.enums.SniperMode;
import com.rich.sol_bot.sniper.enums.SniperStateEnum;
import com.rich.sol_bot.sniper.mapper.*;
import com.rich.sol_bot.sol.RouteScanService;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.ThreadAsyncUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import com.rich.sol_bot.trade.mapper.TokenBaseInfo;
import com.rich.sol_bot.trade.service.TokenInfoService;
import com.rich.sol_bot.user.config.mapper.UserConfig;
import com.rich.sol_bot.user.config.mapper.UserConfigRepository;
import com.rich.sol_bot.wallet.mapper.UserWallet;
import com.rich.sol_bot.wallet.mapper.UserWalletRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SniperGenerateService {
    public SniperPlan generateSniperPlan(Long uid, String tokenAddress, BigDecimal amount, BigDecimal extraGas) {
        TokenBaseInfo baseInfo = tokenInfoService.geneTokenBaseInfo(tokenAddress);
        UserConfig userConfig = userConfigRepository.getById(uid);
        SniperPlan plan = SniperPlan.builder().id(IdUtil.nextId())
                .uid(uid).tokenId(baseInfo.getId()).mainAmount(amount)
                .extraGas(extraGas)
                .mode(SniperMode.fast_mode)
                .liquidity(BigDecimal.ZERO).slippage(userConfig.getFastSlippage())
                .walletCount(1).createdAt(TimestampUtil.now()).state(SniperStateEnum.pre_created)
                .build();
        sniperPlanRepository.save(plan);
        UserWallet userWallet = userWalletRepository.getPreferWallet(uid, userConfig.getPreferWallet());
        this.bindWallet(uid, plan.getId(), userWallet.getId());
        return plan;
    }
    public void setSniperMode(Long uid, Long id, SniperMode mode) {
        UserConfig userConfig = userConfigRepository.getById(uid);
        if(SniperMode.fast_mode.equals(mode)) {
            sniperPlanRepository.update(new LambdaUpdateWrapper<SniperPlan>()
                    .set(SniperPlan::getMode, mode).set(SniperPlan::getSlippage, userConfig.getFastSlippage())
                    .eq(SniperPlan::getId, id).eq(SniperPlan::getUid, uid).eq(SniperPlan::getState, SniperStateEnum.pre_created));
        }
        if(SniperMode.protect_mode.equals(mode)) {
            sniperPlanRepository.update(new LambdaUpdateWrapper<SniperPlan>()
                    .set(SniperPlan::getMode, mode).set(SniperPlan::getSlippage, userConfig.getProtectSlippage())
                    .eq(SniperPlan::getId, id).eq(SniperPlan::getUid, uid).eq(SniperPlan::getState, SniperStateEnum.pre_created));
        }
    }

    public void setSniperExtraGas(Long uid, Long id, BigDecimal extraGas) {
        sniperPlanRepository.update(new LambdaUpdateWrapper<SniperPlan>()
                .set(SniperPlan::getExtraGas, extraGas)
                .eq(SniperPlan::getId, id).eq(SniperPlan::getUid, uid).eq(SniperPlan::getState, SniperStateEnum.pre_created));
    }

    public void finishCreate(Long uid, Long planId, BigDecimal amount) {
        if(sniperPlanRepository.update(new LambdaUpdateWrapper<SniperPlan>()
                        .set(SniperPlan::getState, SniperStateEnum.created)
                        .set(SniperPlan::getMainAmount, amount)
                .eq(SniperPlan::getId, planId).eq(SniperPlan::getState, SniperStateEnum.pre_created))){
            ThreadAsyncUtil.execAsync(() -> {
                routeScanService.recreateTx(planId);
            });
        };
    }

    public void bindWallet(Long uid, Long planId, Long walletId) {
        sniperPlanWalletRepository.save(SniperPlanWallet.builder().id(IdUtil.nextId())
                .uid(uid).planId(planId).walletId(walletId).createdAt(TimestampUtil.now()).build());
    }

    public void bindWalletSwitch(Long uid, Long planId, Long walletId) {
        sniperPlanWalletRepository.remove(new LambdaQueryWrapper<SniperPlanWallet>()
                .eq(SniperPlanWallet::getPlanId, planId));
        sniperPlanWalletRepository.save(SniperPlanWallet.builder().id(IdUtil.nextId())
                .uid(uid).planId(planId).walletId(walletId).createdAt(TimestampUtil.now()).build());
    }

    public void deleteSnipPlan(Long planId) {
        sniperPlanRepository.setDelete(planId);
        routeScanService.removeCreatedTx(planId);
        sniperPlanTxRepository.deleteSniperPlanTx(planId);
    }


    @Resource
    private SniperPlanWalletRepository sniperPlanWalletRepository;
    @Resource
    private SniperPlanRepository sniperPlanRepository;
    @Resource
    private TokenInfoService tokenInfoService;
    @Resource
    private UserConfigRepository userConfigRepository;
    @Resource
    private RouteScanService routeScanService;
    @Resource
    private UserWalletRepository userWalletRepository;
    @Resource
    private SniperPlanTxRepository sniperPlanTxRepository;
}
