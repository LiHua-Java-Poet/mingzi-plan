package com.minzi.plan.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.minzi.common.annotation.OneToOne;
import com.minzi.plan.service.SysMenuService;
import groovy.transform.Field;
import lombok.Data;

/**
 * 角色菜单表
 *
 * @author MinZi
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenuEntity {


    /**
     * ID
     */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

    /**
     * 角色ID
     */
	private Long roleId;

    /**
     * 菜单ID
     */
	private Long menuId;

	@TableField(select = false)
	@OneToOne(localKey = "menu_id",foreignKey = "id",targetService = SysMenuService.class)
	private SysMenuEntity sysMenuEntity;

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