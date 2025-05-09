package com.rich.sol_bot.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BalanceActionEnum {
    increaseRemain,
    reduceRemain,
    freezeRemain,
    unfreezeRemain,
    reduceFreeze,
    increaseFreeze
}
