package com.rich.sol_bot.scraper.mapper;

import com.rich.sol_bot.sniper.enums.SniperMode;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class UserScraperTask {
    private Long id;
    private Long uid;
    private Long twitterUserId; // 表id
    private String twitterUsername;
    private BigDecimal amount;
    private Integer count;
    private Integer successCount;
//    private BigDecimal gas;
    private SniperMode mode;
    private Long walletId;
    private Timestamp createdAt;
    private Integer deleted;
}
