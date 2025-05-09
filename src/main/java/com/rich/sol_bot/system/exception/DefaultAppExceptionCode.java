package com.rich.sol_bot.system.exception;

import lombok.Getter;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Getter
public enum DefaultAppExceptionCode implements AppExceptionCodeTranslate {
    /**
     * 参数错误
     */
    PARAM_ERROR(UNPROCESSABLE_ENTITY.value(), ExceptionType.Parameter, 0, "PARAM_ERROR", "", "参数错误"),
    SYSTEM_ERROR(INTERNAL_SERVER_ERROR.value(), ExceptionType.System, 0, "SYSTEM_ERROR", "", "系统错误"),
    DATABASE_ERROR(INTERNAL_SERVER_ERROR.value(), ExceptionType.Database, 0, "DATABASE_ERROR", "", ""),
    OBJECT_NOT_EXIST(INTERNAL_SERVER_ERROR.value(), ExceptionType.System, 0, "OBJECT_NOT_EXIST", "", "对象不存在"),
    HTTP_REQUEST_ERROR(INTERNAL_SERVER_ERROR.value(), ExceptionType.System, 0, "HTTP_REQUEST_ERROR", "", "http请求错误"),
    UN_LOGIN(INTERNAL_SERVER_ERROR.value(), ExceptionType.ADMIN, 1001, "UN_LOGIN", "", "未登录"),
    LOGIN_FAIL(INTERNAL_SERVER_ERROR.value(), ExceptionType.ADMIN, 1002, "LOGIN_FAIL", "", "登录失败"),
    ADMIN_NOT_EXIST(INTERNAL_SERVER_ERROR.value(), ExceptionType.ADMIN, 1003, "NOT_EXIST", "", "用户不存在"),
    ACCESS_DENY(INTERNAL_SERVER_ERROR.value(), ExceptionType.ADMIN, 0, "ACCESS_DENY", "", "无权限"),

    QUERY_LIMIT(INTERNAL_SERVER_ERROR.value(), ExceptionType.Business, 0, "QUERY_LIMIT", "", "请求频繁"),
    VERIFY_FAIL(INTERNAL_SERVER_ERROR.value(), ExceptionType.Business, 0, "VERIFY_FAIL", "", "验证失败"),
    BOX_NOT_EXIST(INTERNAL_SERVER_ERROR.value(), ExceptionType.Business, 0, "BOX_NOT_EXIST", "", "盲盒不存在"),
    INSUFFiCIENT_BALANCE(INTERNAL_SERVER_ERROR.value(), ExceptionType.Business, 0, "INSUFFiCIENT_BALANCE", "", "余额不足"),
    OPEN_BOX_ERROR(INTERNAL_SERVER_ERROR.value(), ExceptionType.Business, 0, "OPEN_BOX_ERROR", "", "开盲盒失败"),
    REPEAT_PASSWORD_ERROR(INTERNAL_SERVER_ERROR.value(), ExceptionType.Business, 0, "REPEAT_PASSWORD_ERROR", "", "密码不一致"),
    USER_NOT_EXIST(INTERNAL_SERVER_ERROR.value(), ExceptionType.Business, 0, "USER_NOT_EXIST", "", "用户不存在"),
    PASSWORD_ERROR(INTERNAL_SERVER_ERROR.value(), ExceptionType.Business, 0, "PASSWORD_ERROR", "", "密码错误"),
    EMAIL_EXIST(INTERNAL_SERVER_ERROR.value(), ExceptionType.Business, 0, "EMAIL_EXIST", "", "邮箱已存在"),
    CHAIN_NOT_SUPPORT(INTERNAL_SERVER_ERROR.value(), ExceptionType.Business, 0, "CHAIN_NOT_SUPPORT", "","该链不支持"),
    ADDRESS_GENERATE_ERROR(INTERNAL_SERVER_ERROR.value(), ExceptionType.Business, 0, "ADDRESS_GENERATE_ERROR", "", "地址创建失败"),
    WITHDRAW_MIN_LIMIT(INTERNAL_SERVER_ERROR.value(), ExceptionType.Business, 0, "WITHDRAW_MIN_LIMIT", "", "提现余额过少"),
    ADDRESS_ERROR(INTERNAL_SERVER_ERROR.value(), ExceptionType.Business, 0, "ADDRESS_ERROR", "", "地址个是不正确"),


    ;

    private final int httpCode;
    private final ExceptionType exType;
    private final int sn;
    private final String userFacedMsg;
    private final String hideMsg;
    private final String zhMsg;

    DefaultAppExceptionCode(int httpCode, ExceptionType exType, int sn, String userFacedMsg, String hideMsg, String zhMsg) {
        this.httpCode = httpCode;
        this.exType = exType;
        this.sn = sn;
        this.userFacedMsg = userFacedMsg;
        this.hideMsg = hideMsg;
        this.zhMsg = zhMsg;
    }

}
