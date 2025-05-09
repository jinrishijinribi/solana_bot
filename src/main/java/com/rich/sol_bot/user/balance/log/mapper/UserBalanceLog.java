package com.rich.sol_bot.user.balance.log.mapper;

import com.rich.sol_bot.user.enums.BalanceLogTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class UserBalanceLog {
    private Long id;
    private Long uid;
    private String coin;
    private BigDecimal amount;
    private BigDecimal freeze;
    private BigDecimal remain;
    private BalanceLogTypeEnum type;
    private Timestamp createdAt;
    private Long bizId;
}
