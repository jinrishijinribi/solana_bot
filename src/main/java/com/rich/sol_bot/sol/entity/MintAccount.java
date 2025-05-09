package com.rich.sol_bot.sol.entity;

import lombok.Data;

/**
 * @author wangqiyun
 * @since 2024/3/16 17:37
 */

@Data
public class MintAccount {
    private String mintAuthority;
    private String supply;
    private Integer decimals;
    private String freezeAuthority;
}
