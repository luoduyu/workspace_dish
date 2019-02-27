package com.wmt.mgr.controller.wechat.order;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.common.Constants;
import com.wmt.mgr.domain.annotation.PostMappingEx;
import com.wmt.mgr.domain.rabc.permission.MgrModules;
import com.wmt.mgr.service.wechat.order.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Copyright (c) 2019 by CANSHU
 * 商品订单相关业务
 *
 * @author lujunp Create on 2019/2/25 19:33
 * @version 1.0
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class OrderController {

    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Resource
    private OrderService orderService;

    @PostMappingEx(value = "/mgr/order/list", funcName = "订单列表", module = MgrModules.ORDER)
    public BizPacket orderList(String orderId, String submitUserMobile,
                               String poiName, String startTime,
                               String endTime, Integer index,
                               Integer pageSize) {

        if (startTime != null && !startTime.trim().isEmpty()
                && endTime !=null && !endTime.trim().isEmpty()) {
            if (startTime.compareTo(endTime) > 0) {
                return BizPacket.error(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value(), "请求开始时间大于结束时间");
            }
        }

        if (index == null || index < 0 || index >= Integer.MAX_VALUE) {
            index = Constants.INDEX;
        }
        if (pageSize == null || pageSize < 0 || pageSize >= Integer.MAX_VALUE) {
            pageSize = Constants.PAGESIZE;
        }

        return orderService.orderList(orderId, submitUserMobile, poiName, startTime, endTime, index, pageSize);
    }

    @PostMappingEx(value = "/mgr/order/item",funcName = "订单明细", module = MgrModules.ORDER)
    public BizPacket getOrderItemByOrderId(String orderId){

        if (orderId == null || orderId.length() == 0){
            BizPacket.error(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value(),"订单编号不能为空");
        }
        return orderService.getOrderItemByOrderId(orderId);

    }

}
