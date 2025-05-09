package com.rich.sol_bot.chain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Builder
public class ChainBalance {
    // 可读的，算上精度的
    private BigDecimal amount;
}
