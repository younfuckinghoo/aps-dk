package com.aps.yinghai;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("com.aps.yinghai.mapper")
@SpringBootApplication
public class ApsServerApplication {

    public static void main(String[] args) {
//        codeGen();
        SpringApplication.run(ApsServerApplication.class, args);
    }

    public static void codeGen(){
        // 使用 FastAutoGenerator 快速配置代码生成器
        FastAutoGenerator.create("jdbc:mysql://192.168.8.241:3306/aps?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false", "aps_admin", "aps2024")
                .globalConfig(builder -> {
                    builder.author("haoyong") // 设置作者
                            .outputDir("codegen\\src\\main\\java"); // 输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.aps.yinghai") // 设置父包名
                            .entity("entity") // 设置实体类包名
                            .mapper("mapper") // 设置 Mapper 接口包名
                            .service("service") // 设置 Service 接口包名
                            .serviceImpl("service.impl") // 设置 Service 实现类包名
                            .xml("mapper"); // 设置 Mapper XML 文件包名
                })
                .strategyConfig(builder -> {
                    builder.addInclude("BERTH_INFO","BOLLARD_INFO") // 设置需要生成的表名
                            .entityBuilder()
                            .enableLombok() // 启用 Lombok
                            .enableTableFieldAnnotation() // 启用字段注解
                            .controllerBuilder()
                            .enableRestStyle(); // 启用 REST 风格
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用 Freemarker 模板引擎
                .execute(); // 执行生成
    }

}
