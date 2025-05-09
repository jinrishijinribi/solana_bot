package com.rich.sol_bot.admin.stat.mapper;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class StatInvite {
    private Long id;
    private String nickname;
    private String username;
    private Long inviteCount;
    private Long inviteCountToday;
    private Long validCount;
    private BigDecimal tradeAmount;
    private BigDecimal tradeAmountToday;
    private BigDecimal rebate;
    private BigDecimal rebateToday;
    private Timestamp lastUpdateAt;
    private BigDecimal rebateRate;
}