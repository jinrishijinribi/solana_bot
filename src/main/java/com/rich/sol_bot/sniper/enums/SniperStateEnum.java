package com.rich.sol_bot.sniper.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SniperStateEnum {
    pre_created("pre_created"),
    created("created"),
    success("success"),
    fail("fail")
    ;
    @EnumValue
    private final String value;
}
