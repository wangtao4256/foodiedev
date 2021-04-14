package com.imooc.controller;

import com.imooc.enums.PayMethodEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.pojo.bo.SubmitOrderBO;
import com.imooc.pojo.vo.JSONVO;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 订单
 *
 * @author yangxin
 * 2019/12/06 10:23
 */
@RequestMapping("/order")
@RestController
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;


    @PostMapping("/create")
    public JSONVO create(@RequestBody SubmitOrderBO submitOrderBO) {
        log.info("submitOrderQuery: [{}]", submitOrderBO);

        // 判断支付方式
        Integer payMethod = submitOrderBO.getPayMethod();
        if (Objects.equals(payMethod, PayMethodEnum.WEIXIN.getType())
                && Objects.equals(payMethod, PayMethodEnum.ALIPAY.getType())) {
            return JSONVO.errorMsg(ResultEnum.PAY_METHOD_NOT_SUPPORTED.getMessage());
        }

        // 创建订单（并没有对超卖现象做处理）
        OrderVO orderVO = orderService.createOrder(submitOrderBO);
        String orderId = orderVO.getOrderId();

        // 创建订单以后，移除购物车中已结算（已提交）的商品
        /*
          1001
          2002 -> 用户购买
          3003 -> 用户购买
          4004
         */
        // todo 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
//        CookieUtil.setCookie(httpServletRequest, httpServletResponse, FOODIE_SHOP_CART, "", true);

//        log.info("merchantOrdersVO: [{}]", merchantOrdersVO);

        return JSONVO.ok(orderId);
    }

}
