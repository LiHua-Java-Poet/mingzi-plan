package com.minzi.plan.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 字典表
 *
 * @author MinZi
 */
@Data
@TableName("sys_dict")
public class SysDictEntity {


    /**
     * 字典内容ID
     */
	@TableId(value = "id", type = IdType.AUTO)
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