package com.minzi.plan.service.impl;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.minzi.common.utils.DateUtils;
import com.minzi.common.utils.StringUtils;
import com.minzi.plan.common.SysConfigContext;
import com.minzi.plan.service.OSSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;


@Slf4j
@Service
public class OSSServiceImpl implements OSSService {

    @Resource
    private SysConfigContext sysConfigContext;


    @Override
    public String uploadFile(MultipartFile file) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-guangzhou.aliyuncs.com";

        // 手动配置 AccessKeyId 和 AccessKeySecret
        String accessKeyId = sysConfigContext.getConfigContext("accessKeyId"); // 替换为你的 AccessKeyId
        String accessKeySecret = sysConfigContext.getConfigContext("accessKeySecret"); // 替换为你的 AccessKeySecret

        // 使用 DefaultCredentialProvider 手动配置凭证
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);

        // 填写Bucket名称，例如examplebucket。
        String bucketName = "guliwangpan";

        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = DateUtils.currentDate() + "/" + System.currentTimeMillis() + "." + StringUtils.getFileExtension(file.getOriginalFilename());

        // 填写Bucket所在地域。以华东1（杭州）为例，Region填写为cn-hangzhou。
        String region = "cn-guangzhou";

        // 创建OSSClient实例。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();

        try {
            InputStream inputStream = file.getInputStream();
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            log.info("文件上传成功：" + result.getETag());
        } catch (OSSException oe) {
            log.info("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.info("Error Message:" + oe.getErrorMessage());
            log.info("Error Code:" + oe.getErrorCode());
            log.info("Request ID:" + oe.getRequestId());
            log.info("Host ID:" + oe.getHostId());
        } catch (Exception e) {
            log.info("上传文件时发生异常：" + e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return "https://guliwangpan.oss-cn-guangzhou.aliyuncs.com/" + objectName;
    }

}
