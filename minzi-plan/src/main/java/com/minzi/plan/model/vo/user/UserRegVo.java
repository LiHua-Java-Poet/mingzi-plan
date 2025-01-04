package com.minzi.plan.model.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class UserRegVo {

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("账号")
    private String name;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("账号")
    private String phone;

}
