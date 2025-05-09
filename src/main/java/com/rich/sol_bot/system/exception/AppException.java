package com.rich.sol_bot.system.exception;

import lombok.Getter;

public class AppException extends RuntimeException {
    @Getter
    private final AppExceptionCodeTranslate codeTranslate;
    @Getter
    private final boolean printThrow;

    public AppException(AppExceptionCodeTranslate codeTranslate) {
        super();
        this.printThrow = false;
        this.codeTranslate = codeTranslate;
    }

    public AppException(AppExceptionCodeTranslate codeTranslate, Throwable throwable) {
        super(throwable);
        this.printThrow = throwable != null;
        this.codeTranslate = codeTranslate;
    }

    public boolean isCodeTranslate(AppExceptionCodeTranslate translate) {
        return codeTranslate.equals(translate);
    }
}
