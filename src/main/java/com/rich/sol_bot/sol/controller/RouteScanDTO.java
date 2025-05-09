package com.rich.sol_bot.sol.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

/**
 * @author wangqiyun
 * @since 2024/3/23 22:43
 */

@Data
@Builder
public class RouteScanDTO {
    @NotBlank
    private String id;
    @NotBlank
    private String baseMint;
    @NotBlank
    private String quoteMint;
}
