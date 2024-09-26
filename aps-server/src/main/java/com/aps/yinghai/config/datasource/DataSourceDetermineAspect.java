package com.aps.yinghai.config.datasource;

import com.aps.yinghai.enums.DataSourceFlagEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Aspect
@Order(0)
public class DataSourceDetermineAspect {


    @Pointcut("@annotation(com.aps.yinghai.config.datasource.DataSourceFlag)")
    public void dataSourcePointcut(){}

    @Before("dataSourcePointcut()")
    public void beforeMethod(JoinPoint point){
        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method = signature.getMethod();
        DataSourceFlag annotation = method.getAnnotation(DataSourceFlag.class);
        DataSourceFlagEnum flagEnum = annotation.value();
        RoutingDatasource.setDataSourceKey(flagEnum.getDatasourceKey());

    }



}
