package com.rich.sol_bot.trade.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradeSideEnum {
    buy("buy", "买"),
    sell("sell", "卖")
    ;
    @EnumValue
    private final String value;
    private final String zhValue;

}
