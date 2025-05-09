package com.rich.sol_bot.pump.mapper;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PumpPoolInfo {
    private Long id;
    private String mint;
    private String bondingCurve;
    private Integer complete;
    private BigDecimal px;
    private Long createdAt;
}
