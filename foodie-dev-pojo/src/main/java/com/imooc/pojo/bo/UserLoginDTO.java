package com.imooc.pojo.bo;

import lombok.Data;

@Data
public class UserLoginDTO {
    /**
     * 用户名 用户名
     */
    private String username;

    /**
     * 昵称 昵称
     */
    private String nickname;

    /**
     * 头像 头像
     */
    private String face;


    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";
}
