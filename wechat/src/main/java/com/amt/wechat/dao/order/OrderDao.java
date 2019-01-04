package com.amt.wechat.dao.order;

import com.amt.wechat.model.order.OrderData;
import com.amt.wechat.model.order.OrderItemData;
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


    @Select("SELECT * FROM `order` WHERE poiId=#{poiId} LIMIT #{index},#{pageSize}")
    public List<OrderData> getOrderDataList(String poiId,int index, int pageSize);

    @Select("SELECT * FROM `order_item` WHERE orderId=#{orderId}")
    public List<OrderItemData> getOrderDetail(String orderId);
}
