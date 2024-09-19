package com.aps.yinghai.enums;

import com.aps.yinghai.base.BaseEnum;
import com.aps.yinghai.exception.NotFindEnumException;

public enum ShipAlgStateEnum implements BaseEnum {
    NOT_READY(1,"未就绪"),
    READY(2,"已就绪"),
    WORKING(3,"作业中"),
    FINISH(4,"作业已完成");


    private int code;
    private String name;

    ShipAlgStateEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    public static ShipAlgStateEnum getInstanceByCode(int code){
        ShipAlgStateEnum[] values = ShipAlgStateEnum.values();
        for (ShipAlgStateEnum value : values) {
            if (value.getCode() == code)
                return value;
        }
        throw new NotFindEnumException();
    }
}
