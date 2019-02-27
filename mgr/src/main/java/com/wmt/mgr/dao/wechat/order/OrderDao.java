package com.wmt.mgr.dao.wechat.order;

import com.wmt.mgr.model.order.OrderData;
import com.wmt.mgr.model.order.OrderItemData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author lujunp Create on 2019/2/26 13:37
 * @version 1.0
 */
@Repository("orderDao")
@Mapper
public interface OrderDao {


    @Select("<script>" +
            "SELECT * FROM `order` WHERE 1=1 " +
            "<if test=\"orderId != null and orderId.length() != 0 \"> and orderId = #{orderId} </if>" +
            "<if test=\"submitUserMobile != null and submitUserMobile.length()!=0 \"> and submitUserMobile = #{submitUserMobile} </if>" +
            "<if test=\"poiName != null and poiName.length() != 0\"> and poiName LIKE '%${poiName}%' </if>" +
            "<if test=\"startTime != null and startTime.length() != 0\"> and timeEnd &gt;#{startTime}</if>" +
            "<if test=\"endTime != null and endTime.length() != 0\"> and  timeEnd &lt;#{endTime}</if>" +
            "ORDER BY createTime DESC LIMIT #{index},#{pageSize}" +
            "</script>")
    public List<OrderData> getOrderDataList(String orderId, String submitUserMobile,
                                     String poiName, String startTime, String endTime,
                                     int index, int pageSize);
    @Select("<script>" +
            "SELECT count(poiId) FROM `order` WHERE 1=1 " +
            "<if test=\"orderId != null and orderId.length() != 0 \"> and orderId = #{orderId} </if>" +
            "<if test=\"submitUserMobile != null and submitUserMobile.length()!=0 \"> and submitUserMobile = #{submitUserMobile} </if>" +
            "<if test=\"poiName != null and poiName.length() != 0\"> and poiName LIKE '%${poiName}%' </if>" +
            "<if test=\"startTime != null and startTime.length() != 0\"> and timeEnd &gt; #{startTime}</if>" +
            "<if test=\"endTime != null and endTime.length() != 0\"> and timeEnd &lt;#{endTime} </if>" +
            "</script>")
    public Integer countOrderData(String orderId, String submitUserMobile,
                           String poiName, String startTime, String endTime);

    @Select("SELECT * FROM `order` WHERE orderId = #{orderId}")
    public OrderData getOrderDataByOrderId(String orderId);

    @Select("SELECT * FROM order_item WHERE orderId=#{orderId}")
    public List<OrderItemData> getOrderItemByOrderId(String orderId);
}
