package com.rich.sol_bot.sniper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SniperMode {
    fast_mode("fast_mode", "快速模式"),
    protect_mode("protect_mode", "防夹模式")
    ;

    @EnumValue
    private final String value;
    private final String zhValue;
}
