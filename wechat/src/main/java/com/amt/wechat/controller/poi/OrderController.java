package com.amt.wechat.controller.poi;

import com.amt.wechat.controller.base.BaseController;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.form.MyOrderForm;
import com.amt.wechat.model.order.OrderItemData;
import com.amt.wechat.model.order.OrderServiceData;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.service.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-04 19:28
 * @version 1.0
 */
@RestController
public class OrderController extends BaseController {

    private @Resource OrderService orderService;

    @RequestMapping("/order/my")
    public BizPacket findOrderList(Integer index,Integer pageSize){
        if(index == null){
            index = 0;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        PoiUserData userData = getUser();
        List<MyOrderForm> list =  orderService.getOrderDataList(userData.getPoiId(),index,pageSize);

        if(list == null || list.isEmpty()){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"暂时没有订单!");
        }

        return BizPacket.success(list);
    }

    @RequestMapping("/order/detail")
    public BizPacket findOrderList(String orderId){
        if(StringUtils.isEmpty(orderId)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"订单Id为必填项!");
        }

        List<OrderItemData> list =  orderService.getOrderDetail(orderId);
        if(list == null || list.isEmpty()){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"无明细!");
        }
        return BizPacket.success(list);
    }


    @RequestMapping("/order/service")
    public BizPacket getOrderService(@RequestParam("orderId") String orderId){
        if(StringUtils.isEmpty(orderId)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"缺少订单Id");
        }

        OrderServiceData data = orderService.getOrderService(orderId);
        if(data == null){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"暂无评价");
        }
        return BizPacket.success(data);
    }
}