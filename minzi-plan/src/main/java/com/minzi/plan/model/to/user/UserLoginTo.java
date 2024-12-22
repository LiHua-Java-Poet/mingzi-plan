package com.minzi.plan.model.to.user;


import lombok.Data;

@Data
public class UserLoginTo {

    private Long id;

    private String account;

    private String name;

    private String userName;

    private String phone;

    private String headImage;

    private String token;

}
