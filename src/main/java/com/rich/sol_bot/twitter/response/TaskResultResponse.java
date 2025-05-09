package com.rich.sol_bot.twitter.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResultResponse {
    private String id;
    private String fullText;
    private Author author;
    private String createdAt;

    @Data
    @Builder
    public static class Author {
        private String userName;
        private Long id;
    }
}
