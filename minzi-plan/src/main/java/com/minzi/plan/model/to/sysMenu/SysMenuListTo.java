package com.minzi.plan.model.to.sysMenu;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜单目录名
 *
 * @author MinZi
 */
@Data
public class SysMenuListTo implements Serializable {

	private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	private Long id;

    /**
     * 菜单名
     */
	private String menuName;

    /**
     * 菜单路径
     */
	private String navigatorPath;

    /**
     * 图标内容
     */
	private String icon;

    /**
     * 权限字段
     */
	private Integer sort;

    /**
     * 状态 1 正常 2 停用
     */
	private Integer status;

    /**
     * 组件路径
     */
	private String componetPath;

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