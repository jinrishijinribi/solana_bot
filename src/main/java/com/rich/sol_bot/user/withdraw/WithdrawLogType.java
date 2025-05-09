package com.rich.sol_bot.user.withdraw;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WithdrawLogType {
    rebate("rebate"),
    self_wallet("self_wallet")
    ;
    @EnumValue
    private final String value;
}
