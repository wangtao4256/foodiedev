package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public Boolean queryUserNameExists(String userName) {
        QueryWrapper queryWrapper = new QueryWrapper<Users>();
        queryWrapper.eq(Users.USERNAME, userName);
        Users users = usersMapper.selectOne(queryWrapper);
        if (null != users) {
            return true;
        }
        return false;
    }
}
