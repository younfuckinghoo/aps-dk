package com.aps.yinghai.exception;

public class ObjectCloneException extends BaseException{

    public static String preMsg = "克隆异常：";

    public ObjectCloneException(String msg) {
        super(preMsg+msg);
    }

    public ObjectCloneException(String message, Throwable cause) {
        super(preMsg+message, cause);
    }

    public ObjectCloneException(Throwable cause) {
        super(cause);
    }

    public ObjectCloneException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(preMsg+message, cause, enableSuppression, writableStackTrace);
    }
}
