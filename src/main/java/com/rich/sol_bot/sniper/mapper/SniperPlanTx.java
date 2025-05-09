package com.rich.sol_bot.sniper.mapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SniperPlanTx {
    private Long id;
    private Long uid;
    private Long planId;
    private Long walletId;
    private String tx;
    private Long poolStartTime;
    private String txId;
    private Integer success;
}
