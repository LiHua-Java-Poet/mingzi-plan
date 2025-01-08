package com.minzi.plan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
public class Knife4jConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.minzi.plan.controller")) // 控制器包名
                .paths(PathSelectors.any()) // 匹配所有路径
                .build().apiInfo(new ApiInfoBuilder()
                        .title("示例 API 文档")
                        .description("这是一个基于 Spring Boot 的示例 API 文档")
                        .version("1.0")
                        .contact(new Contact("开发者", "https://example.com", "developer@example.com"))
                        .build());
    }

}