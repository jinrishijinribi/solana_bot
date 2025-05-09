package com.rich.sol_bot.user.action;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReturnSource {
    showDealKeyBoard("showDealKeyBoard"),
    showMainConfig("showMainConfig")
    ;
    @EnumValue
    private final String value;
}
