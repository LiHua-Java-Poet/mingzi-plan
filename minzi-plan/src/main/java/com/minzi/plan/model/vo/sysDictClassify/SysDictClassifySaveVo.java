package com.minzi.plan.model.vo.sysDictClassify;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 字典分类表
 *
 * @author MinZi
 */
@Data
public class SysDictClassifySaveVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 父节点ID
	*/
	private Long parentId;

	/**
	 * 字典分类名称
	*/
	private String classifyName;

	/**
	 * 排序
	*/
	private Integer sort;

	/**
	 * 备注说明
	*/
	private String remark;

	/**
	 * 创建时间
	*/
	private Integer createTime;

	/**
	 * 更新时间
	*/
	private Integer updateTime;

	/**
	 * 删除时间
	*/
	private Integer deleteTime;

}