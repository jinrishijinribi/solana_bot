package com.rich.sol_bot.trade.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OHLCVTDTO {
    private String success;
    private DataItem data;

    @Data
    @Builder
    public static class DataItem {
        private List<Item> items;
    }
    @Data
    @Builder
    public static class Item {
        private String o;
        private String h;
        private String l;
        private String c;
        private String v;
        private Long unixTime;
        private String address;
        private String type;
    }
}
