package com.aps.yinghai.exception;

public class NotFindEnumException extends BaseException{

    public static String msg = "不支持所提供的枚举代码，请检查！";

    public NotFindEnumException() {
        super(msg);
    }

    public NotFindEnumException(String msg) {
        super(msg);
    }

    public NotFindEnumException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFindEnumException(Throwable cause) {
        super(cause);
    }

    public NotFindEnumException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
