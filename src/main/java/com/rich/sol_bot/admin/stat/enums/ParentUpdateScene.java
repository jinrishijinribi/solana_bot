package com.rich.sol_bot.admin.stat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ParentUpdateScene {
    invite("invite"),
    trade("trade"),
    all("all")
    ;
    @EnumValue
    private final String value;
}
