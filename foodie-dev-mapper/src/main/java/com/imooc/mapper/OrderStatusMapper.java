package com.imooc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.pojo.OrderStatus;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderStatusMapper extends BaseMapper<OrderStatus> {

    int deleteByPrimaryKey(String orderId);

    int insert(OrderStatus record);

    int insertSelective(OrderStatus record);

    OrderStatus selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(OrderStatus record);

    int updateByPrimaryKey(OrderStatus record);

    List<OrderStatus> select(OrderStatus orderStatus);

    void updateDeliverOrderStatus(OrderStatus orderStatus);

    int updateReceiveOrderStatus(OrderStatus orderStatus);
}