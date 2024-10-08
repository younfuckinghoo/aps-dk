package com.aps.yinghai.enums;

import com.aps.yinghai.base.BaseEnum;
import com.aps.yinghai.exception.NotFindEnumException;

public enum TradeTypeEnum implements BaseEnum {
    IN(1, "内贸"),
    OUT(0, "外贸");


    private int code;
    private String name;

    TradeTypeEnum(int code, String name) {
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

    public static TradeTypeEnum getInstanceByCode(int code) {
        TradeTypeEnum[] values = TradeTypeEnum.values();
        for (TradeTypeEnum value : values) {
            if (value.getCode() == code)
                return value;
        }
        throw new NotFindEnumException();
    }
}
