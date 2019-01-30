package com.amt.wechat.controller.snap;

import com.amt.wechat.controller.base.BaseController;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.form.order.OrderSubmitForm;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.service.snap.SnapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *
 *  抢购订单服务
 * @author adu Create on 2019-01-29 17:54
 * @version 1.0
 */

@RestController(value = "snapOrderController")
public class OrderController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    private @Resource  SnapService snapService;

    @PostMapping(value = "/order/snap/submit",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BizPacket snapOrderSubmit(@RequestBody OrderSubmitForm orderSubmitForm){
        if(orderSubmitForm == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"未收到订单参数!");
        }

        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"非法操作:你没有店铺!");
        }

        if(orderSubmitForm.getOrderItemList() == null || orderSubmitForm.getOrderItemList().isEmpty()){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"订单数据不完整!");
        }

        if(StringUtils.isEmpty(orderSubmitForm.getCateId())){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"缺少参数:cateId");
        }
        return snapService.snapOrderSubmit(userData,orderSubmitForm);
    }

}