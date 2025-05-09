package com.rich.sol_bot.trade.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProfitAndLoseDTO {
    private List<Long> x;
    private List<String> y;
    private List<Long> buyPoint;
    private List<Long> sellPoint;
    private String token;
    private BigDecimal profitLostRate;
    private BigDecimal profit;
    private BigDecimal buyAmount;
    private BigDecimal sellAmount;
    private BigDecimal holdAmount;
    private String image;
    private String imageBase64;
}
