package com.rich.sol_bot.sniper.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rich.sol_bot.system.common.IdUtil;
import com.rich.sol_bot.system.common.TimestampUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SniperPlanWalletRepository extends ServiceImpl<SniperPlanWalletMapper, SniperPlanWallet> {
//    public void bindPlanAndWallet(Long uid, Long planId, Long walletId) {
//        if(this.count(new LambdaQueryWrapper<SniperPlanWallet>()
//                .eq(SniperPlanWallet::getWalletId, walletId)
//                .eq(SniperPlanWallet::getPlanId, planId)) > 0) {
////            this.save(SniperPlanWallet.builder().id(IdUtil.nextId())
////                    .uid(uid).planId(planId).walletId(walletId).createdAt(TimestampUtil.now()).build());
//            this.remove(new LambdaQueryWrapper<SniperPlanWallet>()
//                    .eq(SniperPlanWallet::getWalletId, walletId)
//                    .eq(SniperPlanWallet::getPlanId, planId));
//        } else {
//            this.save(SniperPlanWallet.builder().id(IdUtil.nextId())
//                    .uid(uid).planId(planId).walletId(walletId).createdAt(TimestampUtil.now()).build());
//        }
//    }

    public Long countBind(Long planId) {
        return this.count(new LambdaQueryWrapper<SniperPlanWallet>().eq(SniperPlanWallet::getPlanId, planId));
    }

    public void unbindAll(Long uid, Long planId) {
        this.remove(new LambdaQueryWrapper<SniperPlanWallet>()
                .eq(SniperPlanWallet::getUid, uid)
                .eq(SniperPlanWallet::getPlanId, planId));
    }

    public List<SniperPlanWallet> listAllBind(Long uid, Long planId) {
        return super.list(new LambdaQueryWrapper<SniperPlanWallet>()
                .eq(SniperPlanWallet::getUid, uid)
                .eq(SniperPlanWallet::getPlanId, planId));
    }
}
