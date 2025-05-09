package com.rich.sol_bot.system.common;


public class StringUtils {
    public static String nullToEmpty(String str) {
        if(str == null) return "";
        return str;
    }
}
