package com.rich.sol_bot.scraper.mapper;

import com.rich.sol_bot.sniper.enums.SniperMode;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class UserScraperTaskSub {
    private Long id;
    private Long uid;
    private Long taskId;
    private String tokenAddress;
    private BigDecimal amount;
    private BigDecimal gas;
    private SniperMode mode;
    private Long walletId;
    private Timestamp createdAt;
}
