package com.rich.sol_bot.user.balance.mapper;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class UserBalance {
    private Long id;
    private Long uid;
    private String coin;
    private BigDecimal amount;
    private BigDecimal freeze;
    private BigDecimal remain;
    private Timestamp updatedAt;
}
