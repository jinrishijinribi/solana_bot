package com.rich.sol_bot.admin.stat.dto;

import com.rich.sol_bot.admin.stat.mapper.StatInit;
import com.rich.sol_bot.admin.stat.mapper.StatTradeDaily;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class StatTVView {
    @Schema(description = "时间")
    private Timestamp start;
    @Schema(description = "交易量")
    private BigDecimal vol;

    public static StatTVView transfer(StatTradeDaily item) {
        return StatTVView.builder()
                .start(new Timestamp(item.getId()))
                .vol(item.getTradeAmount())
                .build();
    }
}
