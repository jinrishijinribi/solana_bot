package com.rich.sol_bot.sol.entity;

import lombok.Data;

/**
 * @author wangqiyun
 * @since 2024/3/17 18:41
 */

@Data
public class SignSendResult {
    private String signTransaction;
    private String signTransaction58;
    private String txid;
}
