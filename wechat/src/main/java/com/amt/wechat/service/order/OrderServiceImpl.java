package com.amt.wechat.service.order;

import com.amt.wechat.dao.order.OrderDao;
import com.amt.wechat.form.MyOrderForm;
import com.amt.wechat.model.order.OrderItemData;
import com.amt.wechat.model.order.OrderServiceData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-04 19:26
 * @version 1.0
 */
@Service("orderService")
public class OrderServiceImpl implements  OrderService {

    private @Resource OrderDao orderDao;

    @Override
    public List<MyOrderForm> getOrderDataList(String poiId, int index, int pageSize) {
        //return orderDao.getOrderDataList(poiId,index * pageSize,pageSize);
        return orderDao.getMyOrderList(poiId,index,pageSize);
    }

    @Override
    public List<OrderItemData> getOrderDetail(String orderId) {
        return orderDao.getOrderDetail(orderId);
    }

    @Override
    public OrderServiceData getOrderService(String orderId){
        OrderServiceData data = orderDao.getOrderService(orderId);
        if(data == null){
            return null;
        }
        OrderServiceData total = orderDao.sumServiceScore(data.getServicerId());
        data.setTotalScore(total.getScoreProfess()+total.getScoreService()+total.getScoreResponse());
        return data;
    }
}