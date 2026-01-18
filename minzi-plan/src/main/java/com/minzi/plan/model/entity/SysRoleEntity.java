package com.minzi.plan.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 角色表
 *
 * @author MinZi
 */
@Data
@TableName("sys_role")
public class SysRoleEntity {


    /**
     * ID
     */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

    /**
     * 父角色
     */
	private Long parentId;

    /**
     * 角色名称
     */
	private String roleName;

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