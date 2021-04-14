package com.imooc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.pojo.UserAddress;

import java.util.List;

public interface UserAddressMapper extends BaseMapper<UserAddress> {

    int deleteByPrimaryKey(String id);

    int insert(UserAddress record);

    int insertSelective(UserAddress record);

    UserAddress selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserAddress record);

    int updateByPrimaryKey(UserAddress record);

    /**
     * 根据用户Id，查询用户的收货地址列表
     *
     * @param userId 用户Id
     * @return 收货地址列表
     */
    List<UserAddress> selectByUserId(String userId);
}