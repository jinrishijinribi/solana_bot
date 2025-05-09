package com.rich.sol_bot.limit_order.px_subscribe.mapper;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder
public class AccountSubscribe {
    private Long id;
    private Long tokenId;
    private String ammkey;
    private String baseMint;
    private String quoteMint;
    private BigInteger basePnl;
    private BigInteger quotePnl;
}
