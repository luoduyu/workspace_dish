package com.wmt.mgr.service.wechat.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.common.Constants;
import com.wmt.mgr.dao.wechat.order.OrderDao;
import com.wmt.mgr.model.order.OrderData;
import com.wmt.mgr.model.order.OrderItemData;
import com.wmt.mgr.service.wechat.order.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service("orderService")
public class OrderServiceImpl implements OrderService {


    @Resource
    private OrderDao orderDao;

    @Override
    public BizPacket orderList(String orderId, String submitUserMobile,
                               String poiName, String startTime,
                               String endTime, int index,
                               int pageSize) {

        JSONObject jsonObject = new JSONObject();
        Integer total = orderDao.countOrderData(Constants.delSpace(orderId), Constants.delSpace(submitUserMobile), Constants.delSpace(poiName), startTime, endTime);
        if (total == null) {
            total = 0;
        }
        jsonObject.put("total", total);
        if (total <= 0) {
            jsonObject.put("list", Collections.emptyList());
            return BizPacket.success(jsonObject);
        }
        List<OrderData> list = orderDao.getOrderDataList(orderId, submitUserMobile, poiName, startTime, endTime, index * pageSize, pageSize);
        jsonObject.put("list", list);
        return BizPacket.success(jsonObject);

    }

    @Override
    public BizPacket getOrderItemByOrderId(String orderId) {

        //获取订单基本信息
        OrderData orderData = orderDao.getOrderDataByOrderId(orderId);

        //获取订单-商品信息
        List<OrderItemData> orderItemDataList = orderDao.getOrderItemByOrderId(orderId);

        //获取商品总数量
        OrderItemData orderItemData = orderDao.getTotality(orderId);

        JSONObject jsonObject = new JSONObject();

        if(orderData != null) {
            jsonObject.put("orderId",orderData.getOrderId());
            jsonObject.put("submitUserMobile",orderData.getSubmitUserMobile());
            jsonObject.put("timeEnd",orderData.getTimeEnd());
            jsonObject.put("createTime",orderData.getCreateTime());
            jsonObject.put("payWay",orderData.getPayWay());
            jsonObject.put("payStatus",orderData.getPayStatus());
            jsonObject.put("poiName",orderData.getPoiName());
            jsonObject.put("payment",orderData.getPayment());
            jsonObject.put("total",orderData.getTotal());
        }

        if(orderItemData != null){
            jsonObject.put("totality",orderItemData.getTotality());
        }

        if(orderItemDataList == null || orderItemDataList.size() <= 0){
            jsonObject.put("orderItemDataList",Collections.emptyList());
            return BizPacket.success(jsonObject);
        }
        jsonObject.put("orderItemDataList", orderItemDataList);

        return BizPacket.success(jsonObject);
    }

}
