package com.rich.sol_bot.limit_order.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TokenPxDTO {
    private String ammkey;
    private String mint;
    private BigDecimal px;
    private Long tokenId;
}
