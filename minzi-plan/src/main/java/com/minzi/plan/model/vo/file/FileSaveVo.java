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
public class FileSaveVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 父文件夹ID
	*/
	private Long pid;

	/**
	 * 文件名
	*/
	private String name;

	/**
	 * 文件类型 1 文档 2 文件夹
	*/
	private Integer fileType;

	/**
	 * 唯一码
	 */
	private String uniqueCode;

}