package com.minzi.common.core.model.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

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
     * 用户类型 1 普通用户 2 管理员
     */
    private Integer type;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT) // 插入时自动填充
    private Integer createTime;

    /**
     * 创建时间
     */
    private Integer updateTime;


    @TableLogic(value = "0", delval = "unix_timestamp()")
    @TableField(select = false)
    private Integer deletedTime;

}

