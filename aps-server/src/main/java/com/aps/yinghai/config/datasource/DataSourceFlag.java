package com.aps.yinghai.config.datasource;

import com.aps.yinghai.enums.DataSourceFlagEnum;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceFlag {
    /**
     * 默认算法环绕平台数据库
     * @return
     */
    DataSourceFlagEnum value() default DataSourceFlagEnum.ALGORITHM;

}
