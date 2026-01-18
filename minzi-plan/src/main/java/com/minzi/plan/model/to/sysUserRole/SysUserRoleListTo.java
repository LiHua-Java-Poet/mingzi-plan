package com.minzi.plan.model.to.sysUserRole;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户角色授权表
 *
 * @author MinZi
 */
@Data
public class SysUserRoleListTo implements Serializable {

	private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	private Long id;

    /**
     * 用户ID
     */
	private Long userId;

    /**
     * 角色ID
     */
	private Long roleId;

    /**
     * 创建时间
     */
	private Integer createTime;

    /**
     * 删除时间
     */
	private Integer deleteTime;

}