package com.minzi.plan.controller;


import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.minzi.common.core.query.R;
import com.minzi.common.utils.DateUtils;
import com.minzi.common.utils.StringUtils;
import com.minzi.plan.common.SysConfigContext;
import com.minzi.plan.service.OSSService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Controller
@Api(tags = "对象存储")
public class OSSController {

    @Resource
    private OSSService ossService;

    @PostMapping("/oss/uploadFile")
    @ResponseBody
    public R uploadFile(@RequestParam(name = "file", required = true) MultipartFile file) {
        String filePath = ossService.uploadFile(file);
        return R.ok().setData(filePath);
    }
}
