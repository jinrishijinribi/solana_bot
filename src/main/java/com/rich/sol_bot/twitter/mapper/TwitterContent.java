package com.rich.sol_bot.twitter.mapper;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class TwitterContent {
    private Long id;
    private String postId;
    private String fullText;
    private Long twitterUserId; // 表id
    private String twitterUsername;
    private Timestamp createdAt;
    private Integer confirmed;
}
