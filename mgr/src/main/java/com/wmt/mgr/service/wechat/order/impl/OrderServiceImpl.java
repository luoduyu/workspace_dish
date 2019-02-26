package com.wmt.mgr.service.wechat.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.common.Tools;
import com.wmt.mgr.dao.wechat.order.OrderDao;
import com.wmt.mgr.model.order.OrderData;
import com.wmt.mgr.model.order.OrderItemData;
import com.wmt.mgr.service.wechat.order.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 * 订单相关业务处理
 *
 * @author lujunp Create on 2019/2/25 19:58
 * @version 1.0
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {


    @Resource
    private OrderDao orderDao;

    @Override
    public BizPacket orderList(String orderId, String submitUserMobile,
                               String poiName, String startTime,
                               String endTime, Integer index,
                               Integer pageSize) {

        JSONObject jsonObject = new JSONObject();

        Integer total = orderDao.countOrderData(Tools.delSpace(orderId), Tools.delSpace(submitUserMobile), Tools.delSpace(poiName), startTime, endTime);
        jsonObject.put("total", total);
        if(total==null){
            total = 0;
        }
        if(total <= 0){
            jsonObject.put("list", Collections.emptyList());
            return BizPacket.success(jsonObject);
        }


        List<OrderData> list = orderDao.getOrderDataList(orderId, submitUserMobile, poiName, startTime, endTime, index * pageSize, pageSize);
        jsonObject.put("list", list);
        return BizPacket.success(jsonObject);

    }

    @Override
    public BizPacket getOrderItemByOrderId(String orderId) {

        OrderItemData orderItemData = orderDao.getOrderItemByOrderId(orderId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderItem", orderItemData);

        return BizPacket.success(jsonObject);
    }

}
