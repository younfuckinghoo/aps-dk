package com.aps.yinghai.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;

    private boolean success;

    private T data;

    private String msg;


    public static  Result ok(){
        Result result = new Result();
        result.setCode(200);
        result.setSuccess(true);
        result.setMsg("success");
        return result;
    }

    public static <T> Result<T> ok(Object t,Class<T> clazz){
        Result result = ok();
        result.setData(t);
        return result;
    }

}
