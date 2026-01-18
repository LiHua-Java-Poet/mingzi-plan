package com.minzi.plan.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 字典分类表
 *
 * @author MinZi
 */
@Data
@TableName("sys_dict_classify")
public class SysDictClassifyEntity {


    /**
     * ID
     */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

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
	@TableField(fill = FieldFill.INSERT)
	private Integer createTime;

    /**
     * 更新时间
     */
	private Integer updateTime;

    /**
     * 删除时间
     */
	@TableLogic(value = "0", delval = "unix_timestamp()")
	@TableField(select = false)
	private Integer deleteTime;
}