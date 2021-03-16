package com.imooc.controller;

import com.imooc.service.UserService;
import com.imooc.utils.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/password")
public class PasswordController {
    @Autowired
    private UserService userService;

    public Results usernameIsExist(@RequestParam String username) {
        if (StringUtils.isEmpty(username)) {
            return Results.success();
        }
        Boolean aBoolean = userService.queryUsernameExists(username);
        if (!aBoolean) {
            return Results.error("用户不存在");
        }
        return Results.success();

    }
}
