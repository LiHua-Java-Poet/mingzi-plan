package com.minzi.plan.model.vo.user;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserLoginVo {

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("密码")
    private String password;

}
