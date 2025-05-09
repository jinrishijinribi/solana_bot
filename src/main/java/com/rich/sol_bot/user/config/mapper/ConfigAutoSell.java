package com.rich.sol_bot.user.config.mapper;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class ConfigAutoSell {
    private Long id;
    private Long uid;
    private BigDecimal pxRate;
    private BigDecimal tokenPercent;
    private Timestamp createdAt;
    private Integer deleted;
}
