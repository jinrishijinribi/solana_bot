package com.rich.sol_bot.user.mapper;

import com.rich.sol_bot.user.enums.I18nLanguageEnum;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private Timestamp createdAt;
    private Timestamp lastActive;
    private String refCode;
    private I18nLanguageEnum language;
}
