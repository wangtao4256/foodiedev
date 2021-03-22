package com.imooc.controller;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.CookieUtil;
import com.imooc.utils.JacksonUtil;
import com.imooc.utils.MD5Utils;
import com.imooc.utils.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/passport")
public class PasswordController {
    @Autowired
    private UserService userService;

    @GetMapping("/usernameIsExist")
    public Results usernameIsExist(@RequestParam String username) {
        if (StringUtils.isEmpty(username)) {
            return Results.success();
        }
        Boolean aBoolean = userService.queryUsernameExists(username);
        if (aBoolean) {
            return Results.error("用户名已存在");
        }
        return Results.success();

    }

    @PostMapping("/register")
    public Results register(@RequestBody UserBO userBO) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();
        //判断用户名和密码必须不为空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return Results.error("用户名和密码不能为空");
        }
        //查询用户名是否存在
        Boolean aBoolean = userService.queryUsernameExists(username);
        if (aBoolean) {
            return Results.error("该用户名已存在");
        }

        //判断两次密码是否一致
        if (!password.equals(confirmPassword)) {
            return Results.error("两次输入密码不一致");
        }
        //密码长度不能少于6位
        if (password.length() < 6) {
            return Results.error("密码长度不能少于6");
        }
        return Results.success(userService.createUser(userBO));
    }

    @PostMapping("/login")
    public Results login(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();
        //判断用户名和密码必须不为空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return Results.error("用户名和密码不能为空");
        }
        Users users = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        if (null == users) {
            return Results.error("用户名或密码不正确");
        }
        CookieUtil.setCookie(request, response, "user", JacksonUtil.obj2String(users), true);
        return Results.success(users);
    }

    @PostMapping("/logout")
    public Results logout(@RequestParam String userId, HttpServletResponse response, HttpServletRequest request) throws NoSuchAlgorithmException {
        CookieUtil.deleteCookie(request, response, "user");
        return Results.success();
    }
}
