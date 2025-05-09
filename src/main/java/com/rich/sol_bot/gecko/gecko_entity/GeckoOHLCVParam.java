package com.rich.sol_bot.gecko.gecko_entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeckoOHLCVParam {
    // path
    private String network;
    private String poolAddress;
    private String timeframe;

    // query
    private String aggregate;
    private Long beforeTimestamp; // 结束时间
    private Integer limit;
    private String currency;
    private String token;

}
