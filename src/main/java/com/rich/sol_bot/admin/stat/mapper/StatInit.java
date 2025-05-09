package com.rich.sol_bot.admin.stat.mapper;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class StatInit {
    private Long id;
    private Long regUser;
    private Long regUserToday;
    private BigDecimal tradeAmount;
    private BigDecimal tradeAmountToday;
    private Long tradeUser;
    private Long tradeCount;
    private Long tradeCountToday;
    private Long activeUserToday;
    private Long tradeUserToday;
    private Timestamp lastUpdateAt;
}
