package com.rich.sol_bot.trade.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradeStateEnum {
    created("created"),
    submit("submit"),
    fail("fail"),
    success("success")
    ;
    @EnumValue
    private final String value;
}
