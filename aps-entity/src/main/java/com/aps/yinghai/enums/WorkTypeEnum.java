package com.aps.yinghai.enums;

import com.aps.yinghai.base.BaseEnum;
import com.aps.yinghai.exception.NotFindEnumException;

public enum WorkTypeEnum implements BaseEnum {
    SHIP(1,"船舶");


    private int code;
    private String name;

    WorkTypeEnum(int code, String name){
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

    public static WorkTypeEnum getInstanceByCode(int code){
        WorkTypeEnum[] values = WorkTypeEnum.values();
        for (WorkTypeEnum value : values) {
            if (value.getCode() == code)
                return value;
        }
        throw new NotFindEnumException();
    }
}
