package com.rich.sol_bot.limit_order.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatEnum {
    created("created"),
    submit("submit"),
    success("success"),
    cancel("cancel"),
    fail("fail");

    @EnumValue
    private final String value;
}
