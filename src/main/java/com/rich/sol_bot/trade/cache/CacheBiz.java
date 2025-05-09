package com.rich.sol_bot.trade.cache;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CacheBiz {
    token_px("token_px"),
    token_info("token_info"),
    token_balance("token_balance"),
    amm_key("amm_key"),
    pump_percent("pump_percent"),
    login("login")
    ;
    @EnumValue
    private final String value;
}
