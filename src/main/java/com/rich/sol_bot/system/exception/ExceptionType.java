package com.rich.sol_bot.system.exception;

import lombok.Getter;

public enum ExceptionType {
    /**
     * 参数类异常
     */
    Parameter("P", "参数异常"),
    Business("B", "业务异常"),
    Network("N", "网络异常"),
    Database("D", "数据库异常"),
    ADMIN("A", "管理端异常"),
    /**
     * 系统异常，一般是运行环境异常单靠程序无法恢复的场景，如物理机内存不足
     */
    System("S", "系统异常"),
    /**
     * 其他异常，排除在上述分类中的异常
     */
    Others("A", "其他异常"),
    ;

    @Getter
    private final String symbol;
    @Getter
    private final String description;

    ExceptionType(String symbol, String description) {
        this.symbol = symbol;
        this.description = description;
    }
}
