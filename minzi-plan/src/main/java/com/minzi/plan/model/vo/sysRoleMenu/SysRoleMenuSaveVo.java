package com.minzi.plan.model.vo.sysRoleMenu;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色菜单表
 *
 * @author MinZi
 */
@Data
public class SysRoleMenuSaveVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	*/
	private Long roleId;

	/**
	 * 菜单ID
	*/
	private Long menuId;

}