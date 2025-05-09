package com.rich.sol_bot.sol.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DexscreenerTokenPair {
    private String schemaVersion;
    private List<Pairs> pairs;


    @Data
    @Builder
    public static class Pairs {
        private String chainId;
        private String dexId;
        private String url;
        private String pairAddress;
        private TokenItem baseToken;
        private TokenItem quoteToken;
        private Liquidity liquidity;
        private Long pairCreatedAt;
    }


    @Data
    @Builder
    public static class TokenItem {
        private String address;
        private String name;
        private String symbol;
    }

    @Data
    @Builder
    public static class Liquidity {
        private String usd;
        private Double base;
        private Double quote;
    }
}
