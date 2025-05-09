package com.rich.sol_bot.trade.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConfirmTradeDTO {
    private Boolean success;
    private String errorMsg;
    private Long tradeId;
}
