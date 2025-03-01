package com.minzi.common.config;

import com.minzi.common.tools.EntityAct;
import com.minzi.common.tools.SlidingWindow;
import com.minzi.common.tools.impl.EntityActImpl;
import com.minzi.common.tools.impl.SlidingWindowImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfig {


    @Bean
    public EntityAct getEntityAct(){
        return new EntityActImpl();
    }

    @Bean
    public SlidingWindow getSlidingWindow(){
        return new SlidingWindowImpl();
    }
}
