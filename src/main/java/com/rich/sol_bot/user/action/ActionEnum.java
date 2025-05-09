package com.rich.sol_bot.user.action;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActionEnum {
    wallet("create_wallet"),
    deal("deal"),
    sniper("sniper"),
    position("position"),
    scraper("scraper")
    ;
    @EnumValue
    private final String value;
}
