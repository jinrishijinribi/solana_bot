package com.rich.sol_bot.admin.mapper;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class Admin {
    private Long id;
    private String username;
    private String auth;
    private Timestamp lastLoginAt;
}
