package com.rich.sol_bot.sniper.mapper;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class SniperPlanWallet {
    private Long id;
    private Long uid;
    private Long planId;
    private Long walletId;
    private Timestamp createdAt;
}
