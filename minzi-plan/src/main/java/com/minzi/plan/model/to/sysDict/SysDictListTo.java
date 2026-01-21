package com.minzi.plan.model.to.sysDict;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 字典表
 *
 * @author MinZi
 */
@Data
public class SysDictListTo implements Serializable {

	private static final long serialVersionUID = 1L;

    /**
     * 字典内容ID
     */
	private Long id;

    /**
     * 字典ID
     */
	private Long classifyId;

    /**
     * 字典名称
     */
	private String dictName;

    /**
     * 字典CODE
     */
	private String dictCode;

    /**
     * 排序
     */
	private Integer sort;

    /**
     * 备注说明
     */
	private String remark;

	/**
	 * 颜色
	 */
	private String color;

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