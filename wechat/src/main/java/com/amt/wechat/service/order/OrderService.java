package com.amt.wechat.service.order;

import com.amt.wechat.model.order.OrderData;
import com.amt.wechat.model.order.OrderItemData;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-04 19:25
 * @version 1.0
 */
public interface OrderService {

    public List<OrderData> getOrderDataList(String poiId, int index, int pageSize);
    public List<OrderItemData> getOrderDetail(String orderId);
}
