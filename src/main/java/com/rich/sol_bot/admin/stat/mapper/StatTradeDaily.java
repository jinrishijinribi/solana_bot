package com.rich.sol_bot.admin.stat.mapper;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class StatTradeDaily {
    private Long id;
//    private Timestamp start;
    private BigDecimal tradeAmount;
}
