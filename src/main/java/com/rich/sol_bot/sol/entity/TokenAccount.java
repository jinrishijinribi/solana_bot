package com.rich.sol_bot.sol.entity;

import lombok.Data;

/**
 * @author wangqiyun
 * @since 2024/3/16 17:33
 */

@Data
public class TokenAccount {
    private String amount;
    private String delegatedAmount;
    private Boolean isFrozen;
    private String delegate;
    private String associatedTokenAddress;
}
