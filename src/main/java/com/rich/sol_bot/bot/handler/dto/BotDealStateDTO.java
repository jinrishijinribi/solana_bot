package com.rich.sol_bot.bot.handler.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BotDealStateDTO {
    private Long uid;
    private String tokenAddress;
    private Long walletId;
    private BigDecimal amount;
}
