package com.rich.sol_bot.admin.stat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderValue {
    inviteCount("invite_count"),
    inviteCountToday("invite_count_today"),
    validCount("valid_count"),
    tradeAmount("trade_amount"),
    tradeAmountToday("trade_amount_today"),
    rebate("rebate"),
    rebateToday("rebate_today"),
    rebateRate("rebate_rate")
    ;
    @EnumValue
    private final String value;
}
