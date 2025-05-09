package com.rich.sol_bot.system.common;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class IdUtil {
    private IdUtil() {
    }

    /**
     * 返回去除中划线的uuid
     *
     * @return uuid
     */
    public static String simpleUuid() {
        return uuid().replace("-", "");
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * Long型的ID
     *
     * @return 长整型ID
     */
    public static long nextId() {
        return nextId(System.currentTimeMillis());
    }

    public static long nextId(long timestampMillis) {
        return (timestampMillis << 20) | (ThreadLocalRandom.current().nextLong(0, 1 << 20));
    }

    public static List<Long> nextBatchIds(int nums) {
        Set<Long> idSet = new TreeSet<>();
        while (idSet.size() < nums) {
            idSet.add(nextId());
        }
        return new ArrayList<>(idSet);
    }

    public static void main(String[] args) {
        System.out.println(nextId());
    }
}
