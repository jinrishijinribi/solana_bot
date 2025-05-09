package com.rich.sol_bot.admin.stat.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TradeStatDTO {
    private BigDecimal tradeAmount;
    private BigDecimal tradeRebate;
}
