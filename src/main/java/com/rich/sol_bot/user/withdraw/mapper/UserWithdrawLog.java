package com.rich.sol_bot.user.withdraw.mapper;

import com.rich.sol_bot.trade.enums.TradeStateEnum;
import com.rich.sol_bot.user.withdraw.WithdrawLogType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class UserWithdrawLog {
    private Long id;
    private Long uid;
    private String token;
    private BigDecimal amount;
    private WithdrawLogType type;
    private String fromAddress;
    private String toAddress;
    private String txid;
    private TradeStateEnum state;
    private Timestamp createdAt;
    private Timestamp submitAt;
}
