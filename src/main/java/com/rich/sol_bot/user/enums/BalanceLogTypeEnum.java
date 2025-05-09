package com.rich.sol_bot.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BalanceLogTypeEnum {
    withdraw_freeze("withdraw_freeze", BalanceActionEnum.freezeRemain),
    withdraw_success("withdraw_success", BalanceActionEnum.reduceFreeze),
    withdraw_fail("withdraw_success", BalanceActionEnum.unfreezeRemain),
    rebate("rebate", BalanceActionEnum.increaseRemain)
    ;

    @EnumValue
    private final String value;
    private final BalanceActionEnum action;
}
