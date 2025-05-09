package com.rich.sol_bot.twitter.mapper;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class TwitterScraperTask {
    private Long id;
    private String taskId;
    private Timestamp createdAt;
    private Timestamp finishedAt;
}
