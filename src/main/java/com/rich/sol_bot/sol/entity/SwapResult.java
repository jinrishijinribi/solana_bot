package com.rich.sol_bot.sol.entity;

import lombok.Data;

/**
 * @author wangqiyun
 * @since 2024/3/16 18:30
 */

@Data
public class SwapResult {
    private String swapTransaction;
    private Long lastValidBlockHeight;
    private Long prioritizationFeeLamports;
}
