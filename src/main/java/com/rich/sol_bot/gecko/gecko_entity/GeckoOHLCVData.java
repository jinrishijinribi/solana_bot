package com.rich.sol_bot.gecko.gecko_entity;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class GeckoOHLCVData {
    private String id;
    private String type;
    private Attributes attributes;

    @Data
    @Builder
    public static class Attributes {
        @SerializedName(value = "ohlcv_list")
        private List<List<BigDecimal>> ohlcvList;
    }
}
