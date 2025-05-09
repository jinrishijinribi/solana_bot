package com.rich.sol_bot.admin.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResultDTO {
    @Schema(description = "登录token")
    private String token;
}
