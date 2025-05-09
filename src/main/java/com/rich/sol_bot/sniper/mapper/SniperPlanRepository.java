package com.rich.sol_bot.sniper.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rich.sol_bot.sniper.enums.SniperStateEnum;
import com.rich.sol_bot.user.mapper.UserMapper;
import com.rich.sol_bot.wallet.mapper.UserWalletMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SniperPlanRepository extends ServiceImpl<SniperPlanMapper, SniperPlan> {
    public List<SniperPlan> listByUid(Long uid) {
        return this.list(new LambdaQueryWrapper<SniperPlan>()
                .eq(SniperPlan::getUid, uid).ne(SniperPlan::getState, SniperStateEnum.pre_created)
                .eq(SniperPlan::getDeleted, 0)
        );
    }

    public List<SniperPlan> listCreatedByUid(Long uid) {
        return this.list(new LambdaQueryWrapper<SniperPlan>()
                .eq(SniperPlan::getUid, uid).ne(SniperPlan::getState, SniperStateEnum.pre_created)
                .eq(SniperPlan::getDeleted, 0)
        );
    }

    public void setDelete(Long id) {
        this.update(new LambdaUpdateWrapper<SniperPlan>()
                .set(SniperPlan::getDeleted, 1)
                .eq(SniperPlan::getId, id));
    }

    public long updateWalletCount(Long id, Long count) {
        return sniperPlanMapper.updateWalletCount(id, count);
    }


    @Resource
    private SniperPlanMapper sniperPlanMapper;

}
