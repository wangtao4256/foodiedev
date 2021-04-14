package com.imooc.service.impl;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesNoEnum;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.*;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.AddressService;
import com.imooc.service.ItemService;
import com.imooc.service.OrderService;
import com.imooc.utils.DateUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 订单
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private AddressService addressService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;


    /**
     * 这里的创建订单并没有对“超卖现象”做处理
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderVO createOrder(SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        // 包邮费用设置为0
        int postAmount = 0;

        // 订单id
        String orderId = ObjectId.get().toHexString();

        // 用户订单上的地址
        UserAddress userAddress = addressService.queryUserAddress(userId, addressId);

        // 新订单数据保存
        Orders order = Orders.builder()
                .id(orderId)
                .userId(userId)
                .receiverName(userAddress.getReceiver())
                .receiverMobile(userAddress.getMobile())
                .receiverAddress(userAddress.getProvince() + " "
                        + userAddress.getCity() + " "
                        + userAddress.getDistrict() + " "
                        + userAddress.getDetail())
                .postAmount(postAmount)
                .payMethod(payMethod)
                .leftMsg(leftMsg)
                .isComment(YesNoEnum.NO.getType())
                .isDelete(YesNoEnum.NO.getType())
                .createdTime(new Date())
                .updatedTime(new Date())
                .build();

        // 循环根据itemSpecIds保存订单商品信息表
        // 商品原价累积
        int totalAmount = 0;
        // 优惠后的实际支付价格累计
        int realPayAmount = 0;
        for (String itemSpecId : itemSpecIds.split(",")) {
            // todo 整合redis后，商品购买的数量重新从redis的购物车中获取
            int buyCount = 1;

            // 根据规格id，查询规格的具体信息，主要获取价格
            ItemsSpec itemSpec = itemService.queryItemSpecById(itemSpecId);
            totalAmount += itemSpec.getPriceNormal() * buyCount;
            realPayAmount += itemSpec.getPriceDiscount() * buyCount;

            // 根据商品Id，获取商品信息以及商品图片
            String itemId = itemSpec.getItemId();
            Items item = itemService.queryItemById(itemId);
            ItemsImg itemImg = itemService.queryItemMainImageById(itemId);

            // 循环保存子订单数据到数据库
            OrderItems orderItem = OrderItems.builder()
                    .id(orderId)
                    .orderId(order.getId())
                    .itemId(itemId)
                    .itemName(item.getItemName())
                    .itemImg(itemImg == null ? "" : itemImg.getUrl())
                    .buyCounts(buyCount)
                    .itemSpecId(itemSpecId)
                    .itemSpecName(itemSpec.getName())
                    .price(itemSpec.getPriceDiscount())
                    .build();
            orderItemsMapper.insert(orderItem);

            // 在用户提交订单以后，规格表中需要扣除库存
            itemService.decreaseItemSpecStock(itemSpecId, buyCount);
        }

        order.setTotalAmount(totalAmount);
        order.setRealPayAmount(realPayAmount);
        ordersMapper.insert(order);

        // 保存订单状态表
        OrderStatus orderStatus = OrderStatus.builder()
                .orderId(order.getId())
                .orderStatus(OrderStatusEnum.WAIT_PAY.getType())
                .createdTime(new Date())
                .build();
        orderStatusMapper.insert(orderStatus);

        // 构建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = MerchantOrdersVO.builder()
                .merchantOrderId(orderId)
                .merchantUserId(userId)
                .amount(realPayAmount + postAmount)
                .payMethod(payMethod)
                .build();

        // 构建自定义订单vo
        return OrderVO.builder()
                .orderId(orderId)
                .merchantOrdersVO(merchantOrdersVO)
                .build();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus paidStatus = OrderStatus.builder()
                .orderId(orderId)
                .orderStatus(orderStatus)
                .payTime(new Date())
                .build();

        orderStatusMapper.updateByPrimaryKeySelective(paidStatus);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void closeOrder() {
        // 查询所有未付款订单，判断时间是否超过（1天），超过则关闭交易
//        OrderStatus queryOrder = new OrderStatus();
//        OrderStatus queryOrder = OrderStatus.builder().build();
//        queryOrder.setOrderStatus(OrderStatusEnum.WAIT_PAY.getType());
        OrderStatus queryOrder = OrderStatus.builder()
                .orderStatus(OrderStatusEnum.WAIT_PAY.getType())
                .build();
        List<OrderStatus> orderStatusList = orderStatusMapper.select(queryOrder);

        for (OrderStatus orderStatus : orderStatusList) {
            // 获得订单创建时间
            Date createdTime = orderStatus.getCreatedTime();
            // 和当前时间进行对比
            int days = DateUtil.daysBetween(createdTime, new Date());
            if (days >= 1) {
                // 超过1天，关闭订单
                doCloseOrder(orderStatus.getOrderId());
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    void doCloseOrder(String orderId) {
        OrderStatus orderStatus = OrderStatus.builder()
                .orderId(orderId)
                .orderStatus(OrderStatusEnum.CLOSE.getType())
                .closeTime(new Date())
                .build();
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }
}
