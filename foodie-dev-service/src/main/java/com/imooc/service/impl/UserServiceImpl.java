package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.enums.SexEnum;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.UserService;
import com.imooc.utils.DateUtils;
import com.imooc.utils.MD5Utils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public Boolean queryUsernameExists(String userName) {
        QueryWrapper queryWrapper = new QueryWrapper<Users>();
        queryWrapper.eq(Users.USERNAME, userName);
        Users users = usersMapper.selectOne(queryWrapper);
        if (null != users) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users createUser(UserBO createUserBO) {
        Users users = new Users();
        users.setId(ObjectId.get().toHexString());
        users.setUsername(createUserBO.getUsername());
        users.setNickname(createUserBO.getUsername());
        Date date = DateUtils.strToDate("1900-01-01");
        users.setBirthday(date);
        //性别默认保密
        users.setSex(SexEnum.secret.getType());
        users.setCreatedTime(new Date());
        users.setUpdatedTime(new Date());
        try {
            users.setPassword(MD5Utils.getMD5Str(createUserBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        usersMapper.insert(users);
        return users;
    }

    @Override
    public Users queryUserForLogin(String username, String password) {
        QueryWrapper wrapper = new QueryWrapper<Users>();
        wrapper.eq(Users.USERNAME, username);
        wrapper.eq(Users.PASSWORD, password);
        return usersMapper.selectOne(wrapper);
    }
}
