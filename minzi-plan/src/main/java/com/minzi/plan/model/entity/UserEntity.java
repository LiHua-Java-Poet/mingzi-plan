package com.minzi.plan.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 用户表
 *
 * @author MinZi
 */
@Data
@TableName("user")
public class UserEntity {


    /**
     * 用户主键
     */
	@TableId(value = "id", type = IdType.AUTO)
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

    /**
     * 创建时间
     */
	@TableField(fill = FieldFill.INSERT)
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