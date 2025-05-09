package com.rich.sol_bot.gecko.gecko_entity;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class GeckoTokenBaseInfo {
    private String id;
    private String type;
    private Attributes attributes;
    private Relationships relationships;

    @Data
    @Builder
    public static class Attributes {
        private String address;
        private String name;
        private String symbol;
        @SerializedName(value = "image_url")
        private String imageUrl;
        private String coingeckoCoinId;
        private Integer decimals;
        @SerializedName(value = "total_supply")
        private BigDecimal totalSupply;
        @SerializedName(value = "price_usd")
        private BigDecimal priceUsd;
        @SerializedName(value = "fdv_usd")
        private BigDecimal fdvUsd;
        @SerializedName(value = "total_reserve_in_usd")
        private BigDecimal totalReserveInUsd;
        @SerializedName(value = "market_cap_usd")
        private BigDecimal marketCapUsd;
    }

    @Data
    @Builder
    public static class Relationships {
        @SerializedName(value = "top_pools")
        private TopPools topPools;
    }

    @Data
    @Builder
    public static class TopPools {
        private List<TopPool> data;
    }

    @Data
    @Builder
    public static class TopPool {
        private String id;
        private String type;
    }


}
