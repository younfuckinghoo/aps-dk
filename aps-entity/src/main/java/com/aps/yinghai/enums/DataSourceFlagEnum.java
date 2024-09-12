package com.aps.yinghai.enums;

import com.aps.yinghai.base.BaseEnum;
import com.aps.yinghai.exception.NotFindEnumException;

public enum DataSourceFlagEnum implements BaseEnum {
    ALGORITHM(1, "算法环绕平台数据库","algorithmDatasource"),
    IGTOS(2, "igtos数据库","iGTOSDatasource");


    private int code;
    private String name;
    private String datasourceKey;

    DataSourceFlagEnum(int code, String name,String datasourceKey) {
        this.code = code;
        this.name = name;
        this.datasourceKey = datasourceKey;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDatasourceKey() {
        return datasourceKey;
    }

    public static DataSourceFlagEnum getInstanceByCode(int code) {
        DataSourceFlagEnum[] values = DataSourceFlagEnum.values();
        for (DataSourceFlagEnum value : values) {
            if (value.getCode() == code)
                return value;
        }
        throw new NotFindEnumException();
    }
}
