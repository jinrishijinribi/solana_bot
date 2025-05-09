package com.rich.sol_bot.sol.entity;

import lombok.Data;

import java.util.List;

/**
 * @author wangqiyun
 * @since 2024/3/16 17:53
 */

@Data
public class QuoteResponse {
    private String inputMint;
    private String inAmount;
    private String outputMint;
    private String outAmount;
    private String otherAmountThreshold;
    private String swapMode;
    private Integer slippageBps;
    private PlatformFee platformFee;
    private String priceImpactPct;
    private Long contextSlot;
    private Double timeTaken;
    private List<RoutePlan> routePlan;


    @Data
    public static class RoutePlan {
        private Integer percent;
        private SwapInfo swapInfo;
    }

    @Data
    public static class SwapInfo {
        private String ammKey;
        private String label;
        private String inputMint;
        private String outputMint;
        private String inAmount;
        private String outAmount;
        private String feeAmount;
        private String feeMint;
    }

    @Data
    public static class PlatformFee {
        private String amount;
        private Integer feeBps;
    }
}
