package com.rich.sol_bot.system.exception;

public interface AppExceptionCodeTranslate {
    /**
     * Http状态码
     */
    int getHttpCode();

    /**
     * 错误类型
     */
    ExceptionType getExType();

    /**
     * 错误序列号
     */
    int getSn();

    /**
     * 用户展示的
     */
    String getUserFacedMsg();

    /**
     * 用户展示的中文
     */
    String getZhMsg();

    /**
     * 获取的隐藏的消息，主要给程序内部看禁止输出给用户
     */
    String getHideMsg();

    String name();

    default String errorCode() {
        return String.format("%s_%04d", getExType().getSymbol(), getSn());
    }

    default AppException toException(boolean isThrow, Throwable throwable) {
        if (isThrow) {
            if (throwable == null) {
                throw new AppException(this);
            } else {
                throw new AppException(this, throwable);
            }
        }
        return throwable == null ? new AppException(this) : new AppException(this, throwable);
    }

    default AppException toException(boolean isThrow) {
        return toException(isThrow, null);
    }

    default AppException toException() {
        return toException(false);
    }

    default AppException toException(Throwable throwable) {
        return toException(false, throwable);
    }

    default boolean equals(AppExceptionCodeTranslate translate) {
        if (translate == null) {
            return false;
        }
        if (translate == this) {
            return true;
        }
        return getExType().equals(translate.getExType()) &&
                getSn() == translate.getSn();
    }
}
