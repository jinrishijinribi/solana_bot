package com.rich.sol_bot.sol.entity;

import lombok.Data;

/**
 * @author wangqiyun
 * @since 2024/3/23 17:08
 */

@Data
public class RayComputeAmountOut {
    private String amountOut;//预计输出数量
    private String minAmountOut;//最小输出数量
    private String fee;//预计ray收取手续费
}
