package com.aps.yinghai.exception;

public class ProgramCalculationException extends BaseException{

    public static String preMsg = "程序计算错误";

    public ProgramCalculationException() {
        super(preMsg);
    }

    public ProgramCalculationException(String msg) {
        super(preMsg+":"+msg);
    }

    public ProgramCalculationException(String message, Throwable cause) {
        super(preMsg+":"+message, cause);
    }

    public ProgramCalculationException(Throwable cause) {
        super(cause);
    }

    public ProgramCalculationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(preMsg+":"+message, cause, enableSuppression, writableStackTrace);
    }
}
