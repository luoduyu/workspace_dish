package com.amt.wechat.service.order;

import com.amt.wechat.form.MyOrderForm;
import com.amt.wechat.model.order.OrderItemData;
import com.amt.wechat.model.order.OrderServiceData;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-04 19:25
 * @version 1.0
 */
public interface OrderService {

    /**
     * 数据以下单时间倒序排列
     * @param poiId
     * @param index 0表示第1页,1表示第2页
     * @param pageSize
     * @return
     */
    public List<MyOrderForm> getOrderDataList(String poiId, int index, int pageSize);

    /**
     * 某订单明细
     * @param orderId 订单Id
     * @return
     */
    public List<OrderItemData> getOrderDetail(String orderId);


    public OrderServiceData getOrderService(String orderId);
}
