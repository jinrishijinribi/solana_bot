package com.rich.sol_bot.trade.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PoolInfoDTO {
    private BigDecimal px;
    private BigDecimal solAmount;
    private String ammKey;
    private Long poolStartTime;//池子开放时间,秒级时间戳
    private BigDecimal lpReserve;
    private BigDecimal supply;
}
