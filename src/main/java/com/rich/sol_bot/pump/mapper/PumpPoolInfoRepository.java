package com.rich.sol_bot.pump.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PumpPoolInfoRepository extends ServiceImpl<PumpPoolInfoMapper, PumpPoolInfo> {

    public PumpPoolInfo getByMint(String mint) {
        return this.getOne(new LambdaQueryWrapper<PumpPoolInfo>().eq(PumpPoolInfo::getMint, mint));
    }

    public void completePumpPool(String mint) {
        this.update(new LambdaUpdateWrapper<PumpPoolInfo>().set(PumpPoolInfo::getComplete, 1).eq(PumpPoolInfo::getMint, mint));
    }
}
