package com.minzi.plan.model.vo.sysUserRole;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户角色授权表
 *
 * @author MinZi
 */
@Data
public class SysUserRoleSaveVo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	*/
	private Long userId;

	/**
	 * 角色ID
	*/
	private Long roleId;


}