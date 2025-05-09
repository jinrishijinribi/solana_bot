package com.rich.sol_bot.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssetSupportType {
    ins("ins"),
    ens("ens"),
    token("token")
    ;
    @EnumValue
    private final String value;
}
