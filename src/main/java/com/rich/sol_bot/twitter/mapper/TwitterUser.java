package com.rich.sol_bot.twitter.mapper;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class TwitterUser {
    private Long id;
    private String username;
    private Long twitterUid;
    private String name;
    private Timestamp updatedAt;
    private Integer watched;
}
