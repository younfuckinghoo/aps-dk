package com.aps.yinghai.enums;

import com.aps.yinghai.base.BaseEnum;
import com.aps.yinghai.exception.NotFindEnumException;

public enum ShipStatusEnum implements BaseEnum {
    NOT_READY(1,"未就绪"),
    READY(2,"已就绪"),
    WORKING(3,"作业中"),
    FINISH(4,"作业已完成");


    private int code;
    private String name;

    ShipStatusEnum(int code, String name){
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

    public static ShipStatusEnum getInstanceByCode(int code){
        ShipStatusEnum[] values = ShipStatusEnum.values();
        for (ShipStatusEnum value : values) {
            if (value.getCode() == code)
                return value;
        }
        throw new NotFindEnumException();
    }
}
