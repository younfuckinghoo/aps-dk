package com.aps.yinghai.enums;

import com.aps.yinghai.base.BaseEnum;
import com.aps.yinghai.exception.NotFindEnumException;

public enum LoadUnloadEnum implements BaseEnum {
    LOAD(1, "装"),
    UNLOAD(2, "卸");


    private int code;
    private String name;

    LoadUnloadEnum(int code, String name) {
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

    public static LoadUnloadEnum getInstanceByCode(int code) {
        LoadUnloadEnum[] values = LoadUnloadEnum.values();
        for (LoadUnloadEnum value : values) {
            if (value.getCode() == code)
                return value;
        }
        throw new NotFindEnumException();
    }
}
