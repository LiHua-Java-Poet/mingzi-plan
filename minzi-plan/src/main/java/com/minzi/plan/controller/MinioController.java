package com.minzi.plan.controller;

import com.alibaba.fastjson.JSONObject;
import com.minzi.plan.common.MinioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/File")
public class MinioController {

    @Resource
    private MinioUtils minioUtils;


    /**
     * 上传
     *
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public String upload(@RequestParam(name = "file", required = true) MultipartFile file, HttpServletRequest request) {
        JSONObject res = null;
        try {
            res = minioUtils.uploadFile(file, "images");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("code", 0);
            res.put("msg", "上传失败");
        }
        res.getString("msg");
        return res.toJSONString();
    }
}
