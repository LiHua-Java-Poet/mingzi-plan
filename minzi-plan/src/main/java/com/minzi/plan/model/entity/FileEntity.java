package com.minzi.plan.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 文件表
 *
 * @author MinZi
 */
@Data
@TableName("file")
public class FileEntity {


    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 父文件夹ID
     */
    private Long pid;


    /**
     * 用户ID
     */
    private Long userId;


    /**
     * 文件名
     */
    private String name;


    /**
     * 文件类型 1 文档 2 文件夹
     */
    private Integer fileType;


    /**
     * 文档内容
     */
    private String content;


    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer createTime;


    /**
     * 删除时间
     */
    @TableLogic(value = "0", delval = "unix_timestamp()")
    @TableField(select = false)
    private Integer deleteTime;

}
