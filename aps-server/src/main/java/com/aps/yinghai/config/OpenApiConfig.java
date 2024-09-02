package com.aps.yinghai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("董矿智慧调度算法API")
                        .version("1.0")

                        .description( "DongJiaKou APS(advanced planning and scheduling) system")
                        .termsOfService("http://www.baidu.com")
                        .license(new License().name("Apache 2.0")
                                .url("http://www.baidu.com")));
    }

}
