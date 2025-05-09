package com.rich.sol_bot.bot.check;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberFormatTool {

    /**
     * 数字保留n位有效数字
     * @param formatNumber
     * @param decimals
     * @return
     */
    public static String formatNumber(BigDecimal formatNumber, Integer decimals) {
        try {
            if(formatNumber.compareTo(BigDecimal.ONE) > 0) {
                return formatNumber.setScale(decimals, RoundingMode.HALF_DOWN).stripTrailingZeros().toPlainString();
            } else {
                String input = formatNumber.stripTrailingZeros().toPlainString().replace("0.", "");
                Matcher matcher = Pattern.compile("^(0)+").matcher(input);
                int count = 0;
                if(matcher.find()){
                    count = matcher.group().length();
                }
                return formatNumber.movePointRight(count).setScale(decimals, RoundingMode.HALF_DOWN).movePointLeft(count).stripTrailingZeros().toPlainString();
            }
        }catch (Exception e) {
            return formatNumber.stripTrailingZeros().toPlainString();
        }
    }

    /**
     * 字符串保留n位有效数字
     * @param formatNumberStr
     * @param decimals
     * @return
     */
    public static String formatNumber(String formatNumberStr, Integer decimals) {
        try {
            BigDecimal formatNumber = new BigDecimal(formatNumberStr);
            return formatNumber(formatNumber, decimals);
        }catch (Exception e) {
            return "暂无";
        }
    }

    /**
     * 简化数字 成 M,B,K 之类的
     * @param formatNumber
     * @param decimals
     * @return
     */
    public static String toShort(String formatNumber, Integer decimals) {
        try {
            return toShort(new BigDecimal(formatNumber), decimals);
        }catch (Exception e) {
            return formatNumber;
        }
    }

    private static final List<String> units = Arrays.asList(" ", " K", " M", " B");

    public static String toShort(BigDecimal formatNumber, Integer decimals) {
        try {
            int count = 0;
            while (formatNumber.compareTo(new BigDecimal("1000")) > 0) {
                formatNumber = formatNumber.movePointLeft(3);
                count += 1;
            }
            if(units.size() > count) {
                return formatNumber(formatNumber, decimals) + units.get(count);
            }
            return formatNumber(formatNumber, decimals);
        } catch (Exception e) {
            return formatNumber.stripTrailingZeros().toPlainString();
        }
    }
}
