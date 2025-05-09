package com.rich.sol_bot.bot.handler.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WalletActionEnum {
    to_remove("to_remove"),
    to_set_name("to_set_name"),
    to_export_pri("to_export_pri"),
    to_transfer_sol("to_transfer_sol"),

    ;
    @EnumValue
    private final String value;
}
