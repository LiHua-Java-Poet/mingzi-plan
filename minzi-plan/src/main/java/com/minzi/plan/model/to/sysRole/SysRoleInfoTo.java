package com.minzi.plan.model.to.sysRole;

import com.baomidou.mybatisplus.annotation.*;
import com.minzi.plan.model.to.sysMenu.SysMenuListTo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色表
 *
 * @author MinZi
 */
@Data
public class SysRoleInfoTo implements Serializable {

	private static final long serialVersionUID = 1L;


    /**
     * ID
     */
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
	 * 角色菜单列表
	 */
	List<SysMenuListTo> sysMenuListTos;

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