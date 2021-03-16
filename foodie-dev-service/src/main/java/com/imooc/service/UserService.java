package com.imooc.service;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;

public interface UserService {
    Boolean queryUsernameExists(String userName);

    Users createUser(UserBO createUserBO);

}
