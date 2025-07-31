package com.minzi.common.core.model;


import lombok.Data;

/**
 * 附件文件
 */
@Data
public class AnnexFile {
    /**
     * 附件名
     */
    private String fileName;

    /**
     * 文件后缀类型
     */
    private String fileSuffix;

    /**
     * 文件类型 参考枚举
     */
    private Integer fileType;
}
