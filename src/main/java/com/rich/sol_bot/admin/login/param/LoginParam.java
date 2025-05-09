package com.rich.sol_bot.admin.login.param;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginParam {
    private String username;
    private String code;
}
