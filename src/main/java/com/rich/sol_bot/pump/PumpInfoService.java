package com.rich.sol_bot.pump;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rich.sol_bot.pump.mapper.PumpPoolInfo;
import com.rich.sol_bot.pump.mapper.PumpPoolInfoRepository;
import com.rich.sol_bot.sol.pump.PumpInfo;
import com.rich.sol_bot.sol.pump.PumpOperator;
import com.rich.sol_bot.system.common.IdUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PumpInfoService {

    public PumpPoolInfo getPumpInfo(String mint) {
        PumpPoolInfo poolInfo = pumpPoolInfoRepository.getByMint(mint);
        if(poolInfo != null) return poolInfo;
        PumpInfo pumpInfo = pumpOperator.getPumpInfo(mint);
        if(pumpInfo == null) return null;
        if(pumpInfo.getComplete() == null) return null;
        poolInfo = PumpPoolInfo.builder()
                .id(IdUtil.nextId()).mint(mint)
                .bondingCurve(pumpInfo.getBondingCurve())
                .complete(pumpInfo.getComplete() ? 1 : 0)
                .createdAt(pumpInfo.getCreatedTimestamp())
                .build();
        pumpPoolInfoRepository.save(poolInfo);
        return poolInfo;
    }

    public Boolean pumpComplete(String mint) {
        return pumpPoolInfoRepository.update(new LambdaUpdateWrapper<PumpPoolInfo>().set(PumpPoolInfo::getComplete, 1).eq(PumpPoolInfo::getMint, mint));
    }


    @Resource
    private PumpPoolInfoRepository pumpPoolInfoRepository;
    @Resource
    private PumpOperator pumpOperator;
}
