package com.imooc.pojo.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 订单
 *
 * @author yangxin
 * 2020/03/19 20:49
 */
@Data
@Builder
public class OrderVO {

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 商户订单
     */
    private MerchantOrdersVO merchantOrdersVO;
}
