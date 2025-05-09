package com.rich.sol_bot.trade.mapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenPoolInfo {
    private Long id;
    private String baseMint;
    private String quoteMint;
    private String ammKey;
    private Long poolOpenTime;
    private Long poolCreateTime;
    private String baseTokenAddress;
    private String quoteTokenAddress;
}
