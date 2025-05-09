package com.rich.sol_bot.trade.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenInfoDTO {
    private String symbol;
    // 市值
    private String mc;
    // 价格
    private String price;
    // 已上车车友
    private String onBusCount;
    // mint
    private String isMint;
    // 丢权限
    private String isRenounce;
    // 烧池子
    private String isLiquidityBurn;
    // 持仓top10占比
    private String top10Percent;
    // 创建时间
    private String createdAt;
    // 流动池
    private String liquidity;
    // 链接
    private String twitterUrl;
    private String telegramUrl;
    private String dexScreenerUrl;
    private String birdEyeUrl;

}
