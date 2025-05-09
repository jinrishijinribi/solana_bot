package com.rich.sol_bot.system.common;

/**
 * @author T.J
 * @date 2022/8/25 10:48
 */
public class ByteSizeUtil {
    public static long STEP = 1024;

    public static long ofKB(long kb) {
        return kb * STEP;
    }

    public static long ofMB(long mb) {
        return ofKB(mb * STEP);
    }

    public static long ofGb(long gb) {
        return ofMB(gb * STEP);
    }
}
