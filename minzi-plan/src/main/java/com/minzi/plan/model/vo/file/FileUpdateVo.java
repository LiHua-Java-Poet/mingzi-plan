package com.minzi.plan.model.vo.file;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件表
 *
 * @author MinZi
 */
@Data
public class FileUpdateVo implements Serializable {

	private static final long serialVersionUID = 1L;

    /**
     * ID
     */
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
	private Integer createTime;

    /**
     * 删除时间
     */
	private Integer deleteTime;

}