package com.minzi.plan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@ServletComponentScan
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class MinziPlanApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinziPlanApplication.class, args);
    }

}
