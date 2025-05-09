package com.rich.sol_bot.sol.entity;

import lombok.Data;

/**
 * @author wangqiyun
 * @since 2024/3/16 17:25
 */

@Data
public class CreateWallet {
    private String address;
    private String secret;
}
