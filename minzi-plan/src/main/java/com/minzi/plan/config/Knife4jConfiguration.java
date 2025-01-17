package com.minzi.plan.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.schema.ModelRef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static javax.accessibility.AccessibleRole.HEADER;

@Configuration
public class Knife4jConfiguration {


    @Bean
    public Docket api() {
        // 配置全局参数
        RequestParameter tokenHeader = new RequestParameterBuilder()
                .name("token")
                .description("认证token")
                .in("header")
                .required(true)
                .build();
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.basePackage("com.minzi.plan.controller")) // 替换为你的包路径
                .paths(PathSelectors.any())
                .build()
                .globalRequestParameters(Arrays.asList(tokenHeader));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("今天你自律了吗")
                .description("Knife4j 集成示例")
                .version("1.0")
                .build();
    }




}