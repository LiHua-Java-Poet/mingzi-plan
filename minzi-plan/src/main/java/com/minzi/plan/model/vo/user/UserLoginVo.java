package com.minzi.plan.model.vo.user;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserLoginVo {

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("验证码")
    private String captchaCode;

    @ApiModelProperty("时间戳")
    private String timeToken;
}
