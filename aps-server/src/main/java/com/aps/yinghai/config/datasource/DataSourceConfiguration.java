package com.aps.yinghai.config.datasource;

import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {


    @Bean(name = "algorithmDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.algorithm")
    public DataSource buildAlgorithmDataSource(){
        return DruidDataSourceBuilder.create().build();
    }



    @Bean(name = "iGTOSDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.igtos")
    public DataSource buildIGTOSDataSource(){
        return DruidDataSourceBuilder.create().build();
    }


}
