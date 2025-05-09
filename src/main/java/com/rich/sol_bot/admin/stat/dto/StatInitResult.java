package com.rich.sol_bot.admin.stat.dto;

import com.rich.sol_bot.admin.stat.mapper.StatInit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class StatInitResult {
    @Schema(description = "已注册用户数")
    private Long regUser;
    @Schema(description = "今日新增用户数")
    private Long regUserToday;
    @Schema(description = "总交易额（SOL）")
    private BigDecimal tradeAmount;
    @Schema(description = "今日交易额（SOL）")
    private BigDecimal tradeAmountToday;
    @Schema(description = "有效注册用户")
    private Long tradeUser;
    @Schema(description = "总交易次数")
    private Long tradeCount;
    @Schema(description = "今日交易次数")
    private Long tradeCountToday;
    @Schema(description = "今日活跃人数")
    private Long activeUserToday;
    @Schema(description = "今日交易人数")
    private Long tradeUserToday;
    @Schema(description = "上次更新时间")
    private Timestamp lastUpdateAt;

    public static StatInitResult transfer(StatInit item) {
        return StatInitResult.builder().regUser(item.getRegUser()).regUserToday(item.getRegUserToday())
                .tradeAmount(item.getTradeAmount()).tradeAmountToday(item.getTradeAmountToday())
                .tradeUser(item.getTradeUser()).tradeCount(item.getTradeCount())
                .tradeCountToday(item.getTradeCountToday()).activeUserToday(item.getActiveUserToday())
                .tradeUserToday(item.getTradeUserToday())
                .lastUpdateAt(item.getLastUpdateAt())
                .build();
    }


}
