package com.rich.sol_bot.system.common;

import io.swagger.v3.oas.annotations.media.Schema;


public record RestControllerResult(@Schema(description = "错误码") String code,
                                   @Schema(description = "描述信息") String msg,
                                   @Schema(description = "业务数据") Object data) {
    public RestControllerResult(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static RestControllerResult success(Object data) {
        return new RestControllerResult("Success", null, data);
    }
}
