package com.imooc.controller;

import com.imooc.enums.ResultEnum;
import com.imooc.pojo.bo.ShopCartBO;
import com.imooc.pojo.vo.JSONVO;
import com.imooc.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/shopcart")
@RestController
@Slf4j
public class ShopCartController {
    @Autowired
    private ItemService itemService;

    @PostMapping("/add")
    public JSONVO add(@RequestParam String userId,
                      @RequestBody ShopCartBO shopCartBO,
                      HttpServletRequest httpServletRequest,
                      HttpServletResponse httpServletResponse) {
        if (StringUtils.isEmpty(userId)) {
            return JSONVO.errorMsg("");
        }

        // todo 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存

        return JSONVO.ok();
    }

    @PostMapping("/del")
    public JSONVO delete(@RequestParam String userId,
                         @RequestParam String itemSpecId) {
        log.info("userId: [{}], itemSpecId: [{}]", userId, itemSpecId);

        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(itemSpecId)) {
            return JSONVO.errorMsg(ResultEnum.PARAMETER_CANT_EMPTY.getMessage());
        }

        // todo 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除后端购物车项
        return JSONVO.ok();
    }
}
