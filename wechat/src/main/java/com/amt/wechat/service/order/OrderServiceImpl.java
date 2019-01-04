package com.amt.wechat.service.order;

import com.amt.wechat.dao.order.OrderDao;
import com.amt.wechat.model.order.OrderData;
import com.amt.wechat.model.order.OrderItemData;
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
    public List<OrderData> getOrderDataList(String poiId, int index, int pageSize) {
        return orderDao.getOrderDataList(poiId,index * pageSize,pageSize);
    }

    @Override
    public List<OrderItemData> getOrderDetail(String orderId) {
        return orderDao.getOrderDetail(orderId);
    }
}