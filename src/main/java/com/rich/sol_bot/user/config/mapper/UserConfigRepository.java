package com.rich.sol_bot.user.config.mapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rich.sol_bot.system.common.TimestampUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserConfigRepository extends ServiceImpl<UserConfigMapper, UserConfig> {


    public void generateDefaultConfig(Long uid, BigDecimal rebateRate, BigDecimal feeRate) {
        userConfigMapper.insert(UserConfig.builder()
                .id(uid).fastSlippage(new BigDecimal("0.2"))
                .rebateRate(rebateRate).feeRate(feeRate)
                .protectSlippage(new BigDecimal("0.49")).buyFee(new BigDecimal("0.005"))
                .sellFee(new BigDecimal("0.005")).sniperFee(new BigDecimal("0.005"))
                .createdAt(TimestampUtil.now()).updatedAt(TimestampUtil.now())
                .build());
    }

    @Resource
    private UserConfigMapper userConfigMapper;

}
