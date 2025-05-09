package com.rich.sol_bot.sol.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wangqiyun
 * @since 2024/3/21 21:04
 */

@Data
public class RaydiumMint {
    private BigDecimal lpReserve;
    private String baseMint;//交易对基础代币
    private String basePnl;
    private String quotePnl;
    private String quoteMint;//交易对另一个代币
    private String poolOpenTime;//池子开放时间,秒级时间戳
    private MintAccount lpMint;
    private TokenAccount baseVault;
    private TokenAccount quoteVault;
}
