package com.rich.sol_bot.trade.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradeDexEnum {
    ray("ray"),
    pump("pump")
    ;
    @EnumValue
    private final String value;
}
