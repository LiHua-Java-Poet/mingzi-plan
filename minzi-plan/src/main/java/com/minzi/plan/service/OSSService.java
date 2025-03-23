package com.minzi.plan.service;

import org.springframework.web.multipart.MultipartFile;

public interface OSSService {

    /**
     * 上传一个文件
     * @param file 文件
     * @return 文件的存储路径
     */
    String uploadFile(MultipartFile file);
}
