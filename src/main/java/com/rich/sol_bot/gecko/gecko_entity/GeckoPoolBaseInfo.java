package com.rich.sol_bot.gecko.gecko_entity;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
public class GeckoPoolBaseInfo {
    private String id;
    private String type;
    private Attributes attributes;
    private Relationships relationships;

    @Data
    @Builder
    public static class Attributes {
        @SerializedName(value = "base_token_price_usd")
        private String baseTokenPriceUsd;
        @SerializedName(value = "base_token_price_native_currency")
        private String baseTokenPriceNativeCurrency;
        @SerializedName(value = "quote_token_price_usd")
        private String quoteTokenPriceUsd;
        @SerializedName(value = "quote_token_price_native_currency")
        private String quoteTokenPriceNativeCurrency;
        @SerializedName(value = "base_token_price_quote_token")
        private String baseTokenPriceQuoteToken;
        @SerializedName(value = "quote_token_price_base_token")
        private String quoteTokenPriceBaseToken;
        private String address;
        private String name;
        @SerializedName(value = "pool_created_at")
        private Timestamp poolCreatedAt;
        @SerializedName(value = "fdv_usd")
        private String fdvUsd;
        @SerializedName(value = "market_cap_usd")
        private String marketCapUsd;
        @SerializedName(value = "reserve_in_usd")
        private BigDecimal reserveInUsd;
    }

    @Data
    @Builder
    public static class Relationships {
        @SerializedName(value = "base_token")
        private TokenItemData baseToken;
        @SerializedName(value = "quote_token")
        private TokenItemData quoteToken;
        private Dex dex;
    }

    @Data
    @Builder
    public static class TokenItemData {
        private TokenItem data;
    }

    @Data
    @Builder
    public static class TokenItem {
        private String id;
        private String type;
    }

    @Data
    @Builder
    public static class Dex {
        private DexItem data;
    }

    @Data
    @Builder
    public static class DexItem {
        private String id;
        private String type;
    }
}
