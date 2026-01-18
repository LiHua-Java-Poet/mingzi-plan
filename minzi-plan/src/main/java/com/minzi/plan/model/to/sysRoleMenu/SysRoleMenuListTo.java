package com.minzi.plan.model.to.sysRoleMenu;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色菜单表
 *
 * @author MinZi
 */
@Data
public class SysRoleMenuListTo implements Serializable {

	private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	private Long id;

    /**
     * 角色ID
     */
	private Long roleId;

    /**
     * 菜单ID
     */
	private Long menuId;

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