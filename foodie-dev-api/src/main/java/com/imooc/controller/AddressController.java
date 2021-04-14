package com.imooc.controller;


import com.imooc.enums.ResultEnum;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.pojo.vo.JSONVO;
import com.imooc.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 地址
 *
 * @author yangxin
 * 2019/12/04 20:40
 */
@RestController
@RequestMapping("/address")
@Slf4j
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    /*
      用户在确认订单页面，可以针对收货地址做如下操作：
      1. 查询用户的所有收货地址列表
      2. 新增收货地址
      3. 删除收货地址
      4. 修改收货地址
      5. 设置默认地址
     */

    /**
     * 查询用户的所有收货地址列表
     */
    @PostMapping("/list")
    public JSONVO list(@RequestParam String userId) {
        log.info("userId: [{}]", userId);

        if (StringUtils.isEmpty(userId)) {
            return JSONVO.errorMsg("");
        }

        return JSONVO.ok(addressService.queryAll(userId));
    }

    /**
     * 新增收货地址
     */
    @PostMapping("/add")
    public JSONVO add(@RequestBody @Valid AddressBO addressBO, BindingResult bindingResult) {
        log.info("addressQuery: [{}]", addressBO);

        if (bindingResult.hasErrors()) {
            log.error(ResultEnum.PARAMETER_ERROR.getMessage());

            FieldError fieldError = bindingResult.getFieldError();
            return JSONVO.errorMsg(fieldError == null ? "" : fieldError.getDefaultMessage());
        }

        addressService.addNewUserAddress(addressBO);
        return JSONVO.ok();
    }

    /**
     * 修改收货地址
     */
    @PostMapping("/update")
    public JSONVO update(@RequestBody @Valid AddressBO addressBO, BindingResult bindingResult) {
        log.info("addressQuery: [{}]", addressBO);

        if (StringUtils.isEmpty(addressBO.getAddressId())) {
            return JSONVO.errorMsg(ResultEnum.PARAMETER_ERROR.getMessage());
        }

        if (bindingResult.hasErrors()) {
            log.error(ResultEnum.PARAMETER_ERROR.getMessage());

            FieldError fieldError = bindingResult.getFieldError();
            return JSONVO.errorMsg(fieldError == null ? "" : fieldError.getDefaultMessage());
        }

        addressService.updateUserAddress(addressBO);
        return JSONVO.ok();
    }

    /**
     * 删除收货地址
     */
    @PostMapping("/delete")
    public JSONVO delete(@RequestParam String userId, @RequestParam String addressId) {
        log.info("userId: [{}], addressId: [{}]", userId, addressId);

        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(addressId)) {
            return JSONVO.errorMsg("");
        }

        addressService.deleteUserAddress(addressId);
        return JSONVO.ok();
    }

    /**
     * 设置默认收货地址
     */
    @PostMapping("/setDefalut")
    public JSONVO setDefault(@RequestParam String userId, @RequestParam String addressId) {
        log.info("userId: [{}], addressId: [{}]", userId, addressId);

        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(addressId)) {
            return JSONVO.errorMsg("");
        }

        addressService.updateUserAddressAsDefault(userId, addressId);
        return JSONVO.ok();
    }
}

