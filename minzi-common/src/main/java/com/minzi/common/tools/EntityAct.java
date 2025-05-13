package com.minzi.common.tools;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class EntityAct implements OneToOneAct, OneToManyAct {

    @Resource
    private ApplicationContext applicationContext;

}
