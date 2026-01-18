package com.minzi.plan.model.to.user;

import com.baomidou.mybatisplus.annotation.*;
import com.minzi.plan.model.to.sysRole.SysRoleInfoTo;
import com.minzi.plan.model.to.sysRole.SysRoleListTo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户表
 *
 * @author MinZi
 */
@Data
public class UserListTo implements Serializable {

	private static final long serialVersionUID = 1L;

    /**
     * 用户主键
     */
	private Long id;

    /**
     * 账号
     */
	private String account;

    /**
     * 用户名
     */
	private String userName;

    /**
     * 手机号
     */
	private String phone;

    /**
     * 密码
     */
	private String password;

    /**
     * 状态 (1 正常, 2 停用)
     */
	private Integer status;

    /**
     * 头像地址
     */
	private String headImage;

    /**
     * 名字
     */
	private String name;

	List<SysRoleInfoTo> roleList;

    /**
     * 创建时间
     */
	private Integer createTime;

    /**
     * 逻辑删除标记，0 表示未删除，unix_timestamp() 表示删除时间
     */
	private Integer deletedTime;

    /**
     * 1 普通用户 2 管理员
     */
	private Integer type;

}