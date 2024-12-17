package com.minzi.plan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class MinziPlanApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinziPlanApplication.class, args);
    }

}
