package com.aps.yinghai.config.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@ConditionalOnBean(name = {"algorithmDatasource","iGTOSDatasource"})
@Primary
@Component
public class RoutingDatasource extends AbstractRoutingDataSource {

    public static final ThreadLocal<String> dataSourceKeyLocal = new ThreadLocal<>();

    public RoutingDatasource(DataSource algorithmDatasource, DataSource iGTOSDatasource) {
        this.algorithmDatasource = algorithmDatasource;
        this.iGTOSDatasource = iGTOSDatasource;
    }

    public static void setDataSourceKey(String dataSourceKey){
        dataSourceKeyLocal.set(dataSourceKey);
    }

    public String getDataSourceKey(){
        return dataSourceKeyLocal.get();
    }




    @Qualifier("algorithmDatasource")
    private final DataSource algorithmDatasource;


    @Qualifier("iGTOSDatasource")
    private final DataSource iGTOSDatasource;

    /**
     * 配置初始数据源信息
     */
    @Override
    public void afterPropertiesSet() {
        Map<Object, Object> map = new HashMap<>();
        map.put("algorithmDatasource", algorithmDatasource);
        map.put("iGTOSDatasource", iGTOSDatasource);
        setTargetDataSources(map);
        setDefaultTargetDataSource(algorithmDatasource);
        super.afterPropertiesSet();
    }


    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceKeyLocal.get();
    }
}
