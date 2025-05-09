package com.rich.sol_bot.trade.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;

@Getter
@AllArgsConstructor
public enum OHLCVTypeEnum {
    m1("1m", 60L, "minute", "1"),
    //    m3("3m", 3 * 60L, "minute", "3"),
    m5("5m", 5 * 60L, "minute", "5"),
    m15("15m", 15 * 60L, "minute", "15"),
    //    m30("30m", 30 * 60L, "minute", "30"),
    H1("1H", 60 * 60L, "hour", "1"),
    //    H2("2H", 2 * 60 * 60L, "hour", "2"),
    H4("4H", 4 * 60 * 60L, "hour", "4"),
    //    H6("6H", 6 * 60 * 60L, "hour", "6"),
    H6("12H", 12 * 60 * 60L, "hour", "12"),
    D1("1d", 24 * 60 * 60L, "day", "1")
//    H8("8H", 8 * 60 * 60L),
//    H12("12H", 12 * 60 * 60L),
//    D1("1D", 24 * 60 * 60L),
//    D3("3D", 3 * 24 * 60 * 60L),
//    W1("1W", ),
//    M1("1M")
    ;
    @EnumValue
    private final String value;
    @EnumValue
    private final Long second;
    @EnumValue
    private final String timeframe;
    @EnumValue
    private final String aggregate;

    public static OHLCVTypeEnum calculateOne(Long start, Long end, Long count) {
        Long diff = (end - start) / count;
        OHLCVTypeEnum result = D1;
        for(OHLCVTypeEnum item : Arrays.stream(OHLCVTypeEnum.values()).sorted(Comparator.comparing(OHLCVTypeEnum::getSecond).reversed()).toList()) {
            if(diff < item.second) {
                result = item;
            }
        }
        return result;
    }
//    m1("1m", 60L),
//    m3("3m", 3 * 60L),
//    m5("5m", 5 * 60L),
//    m15("15m", 15 * 60L),
//    m30("30m", 30 * 60L),
//    H1("1H", 60 * 60L),
//    H2("2H", 2 * 60 * 60L),
//    H4("4H", 4 * 60 * 60L),
//    H6("6H", 6 * 60 * 60L)
////    H8("8H", 8 * 60 * 60L),
////    H12("12H", 12 * 60 * 60L),
////    D1("1D", 24 * 60 * 60L),
////    D3("3D", 3 * 24 * 60 * 60L),
////    W1("1W", ),
////    M1("1M")
//    ;
//    @EnumValue
//    private final String value;
//    @EnumValue
//    private final Long second;
//
//    public static OHLCVTypeEnum calculateOne(Long start, Long end, Long count) {
//        Long diff = (end - start) / count;
//        OHLCVTypeEnum result = m1;
//        for(OHLCVTypeEnum item : Arrays.stream(OHLCVTypeEnum.values()).sorted(Comparator.comparing(OHLCVTypeEnum::getSecond)).toList()) {
//            if(diff > item.second) {
//                result = item;
//            }
//        }
//        return result;
//    }
}
