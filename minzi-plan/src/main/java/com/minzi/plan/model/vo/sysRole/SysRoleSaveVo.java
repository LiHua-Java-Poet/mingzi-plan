package com.minzi.plan.model.vo.sysRole;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色表
 *
 * @author MinZi
 */
@Data
public class SysRoleSaveVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 父角色
	*/
	private Long parentId;

	/**
	 * 角色名称
	*/
	private String roleName;

}