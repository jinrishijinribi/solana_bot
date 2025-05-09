package com.rich.sol_bot.bot.handler.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BotWalletCreateDTO {
    private Long uid;
    private String name;
    private String priKey;
}
