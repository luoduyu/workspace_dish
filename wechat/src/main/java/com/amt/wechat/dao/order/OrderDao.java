package com.amt.wechat.dao.order;

import com.amt.wechat.form.MyOrderForm;
import com.amt.wechat.model.order.OrderItemData;
import com.amt.wechat.model.order.OrderServiceData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-04 19:20
 * @version 1.0
 */
@Repository("orderDao")
@Mapper
public interface OrderDao {




    public List<MyOrderForm> getMyOrderList(String poiId,int index, int pageSize);

    @Select("SELECT * FROM `order_item` WHERE orderId=#{orderId}")
    public List<OrderItemData> getOrderDetail(String orderId);

    @Select("SELECT orderId,servicerId,servicerName,commentText,score_service as scoreService ,score_profess as scoreProfess,score_response as scoreResponse FROM order_service WHERE orderId=#{orderId}")
    public OrderServiceData getOrderService(String orderId);


    @Select("SELECT SUM(score_service) as scoreService,SUM(score_profess) AS scoreProfess,sum(score_response) AS scoreResponse FROM order_service WHERE servicerId=#{servicerId}")
    public OrderServiceData sumServiceScore(int servicerId);
}
