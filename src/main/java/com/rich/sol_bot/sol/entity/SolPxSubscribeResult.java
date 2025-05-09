package com.rich.sol_bot.sol.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SolPxSubscribeResult {
    private SubscribeItem base;
    private SubscribeItem quote;

    @Data
    @Builder
    public static class SubscribeItem {
        private String mint;
        private String address;
        private Integer submitId;
    }
}
