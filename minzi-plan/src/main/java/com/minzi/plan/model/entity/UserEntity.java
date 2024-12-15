package com.minzi.plan.model.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class UserEntity {

    /**
     * 用户主键
     */
    private String userPk;

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
    private int status;

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
    private int createTime;

}
