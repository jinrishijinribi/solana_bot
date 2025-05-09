package com.rich.sol_bot.admin.stat.params;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SetRebateRateParam {
    @Schema(description = "用户id")
    private Long id;
    @Schema(description = "返佣比例, 0 - 1")
    @Max(value = 1)
    @Min(value = 0)
    private BigDecimal rate;
}
