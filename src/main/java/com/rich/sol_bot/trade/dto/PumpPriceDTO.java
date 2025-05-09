package com.rich.sol_bot.trade.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PumpPriceDTO {
    private BigDecimal px;
    private Boolean complete;
    private BigDecimal solAmount;
}
