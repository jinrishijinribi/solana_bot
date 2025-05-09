package com.rich.sol_bot.user.action.mapper;

import com.rich.sol_bot.user.action.ActionEnum;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class UserAction {
    private Long id;
    private Long uid;
    private Integer messageId;
    private ActionEnum type;
    private String name;
    private String value;
    private Timestamp createdAt;
}
