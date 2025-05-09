package com.rich.sol_bot.trade.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UriInfoDTO {
    private String name;
    private String symbol;
    private String description;
    private Extensions extensions;
    private Creator creator;
    private String image;
    private String twitter;
    private String telegram;


    @Data
    @Builder
    public static class Extensions {
        private String discord;
        private String twitter;
        private String website;
        private String telegram;
    }

    @Data
    @Builder
    public static class Creator {
        private String name;
        private String site;
    }
}
