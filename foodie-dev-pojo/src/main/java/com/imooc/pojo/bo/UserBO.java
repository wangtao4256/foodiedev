package com.imooc.pojo.bo;

import lombok.Data;

@Data
public class UserBO {
    private String username;
    private String password;
    private String confirmPassword;
}
