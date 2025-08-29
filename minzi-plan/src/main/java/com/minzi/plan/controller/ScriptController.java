package com.minzi.plan.controller;

import com.alibaba.fastjson.JSONObject;
import com.minzi.common.core.loadClass.LoadJarUtils;
import com.minzi.common.core.query.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

@RestController
@RequestMapping("/app/script")
public class ScriptController {

    @GetMapping("/demo")
    public R getUserDataTo() throws Exception {
        try {
            LoadJarUtils.loadJar("C:\\Users\\Administrator\\IdeaProjects\\mingzi-plan\\minzi-plan\\src\\main\\resources\\script/minzi-scprit-1.0.0.jar");
            Class<?> clazz = LoadJarUtils.getUrlClassLoader().loadClass("ScriptExample");
            Object o = clazz.newInstance();
            Method batchExportMethod = clazz.getMethod("demo");
            Object invoke = batchExportMethod.invoke(o);
        } finally {
            LoadJarUtils.unloadJar();
        }
        return R.ok();
    }
}
