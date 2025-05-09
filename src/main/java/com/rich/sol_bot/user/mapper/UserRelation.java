package com.rich.sol_bot.user.mapper;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class UserRelation {
    private Long id;
    private Long pid;
    private Timestamp createdAt;
}
