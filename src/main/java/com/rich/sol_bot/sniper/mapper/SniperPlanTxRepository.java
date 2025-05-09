package com.rich.sol_bot.sniper.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SniperPlanTxRepository extends ServiceImpl<SniperPlanTxMapper, SniperPlanTx> {
    public void deleteSniperPlanTx(Long planId) {
        this.remove(new LambdaQueryWrapper<SniperPlanTx>().eq(SniperPlanTx::getPlanId, planId));
    }
}
