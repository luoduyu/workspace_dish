package com.amt.wechat.dao.order;

import com.amt.wechat.model.order.MyOrderForm;
import com.amt.wechat.model.order.OrderData;
import com.amt.wechat.model.order.OrderItemData;
import com.amt.wechat.model.order.OrderServiceData;
import org.apache.ibatis.annotations.*;
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

    @Insert("INSERT INTO `order`(poiId,orderId,goodsType,total,couponPaid,balancePaid,wechatPaid,payment,payStatus,createTime,payTime,serviceStatus) VALUES(#{poiId},#{orderId},#{goodsType},#{total},#{couponPaid},#{balancePaid},#{wechatPaid},#{payment},#{payStatus},#{createTime},#{payTime},#{serviceStatus})")
    public void addOrderData(OrderData orderData);

    @Select("SELECT * FROM `order` WHERE orderId=#{orderId}")
    public OrderData getOrder(String orderId);

    @Delete("DELETE FROM `order` WHERE orderId=#{orderId}")
    public void removeOrder(String orderId);


    @Update("UPDATE `order` SET total =#{total} WHERE orderId=#{orderId}")
    public void updateTotal(String id,int total);

    public List<MyOrderForm> getMyOrderList(String poiId,int index, int pageSize);

    @Select("SELECT COUNT(*) FROM `order` WHERE poiId=#{poiId}")
    public int countMyOrder(String poiId);


    @Delete("DELETE FROM order_item WHERE orderId=#{orderId}")
    public void removeOrderItemByOrderId(String orderId);






    @Insert("INSERT INTO order_item (orderId,goodsType,goodsId,goodsName,imgUrl,num,unitPrice,total) VALUES(#{orderId},#{goodsType},#{goodsId},#{goodsName},#{imgUrl},#{num},#{unitPrice},#{total})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int addOrderItemData(OrderItemData orderItemData);


    public void addOrderItemDataList(List<OrderItemData> orderItemDataList);

    @Delete("DELETE FROM order_item WHERE id=#{id}")
    public void deleteOrderItemData(int id);

    @Update("UPDATE order_item SET num = #{num}, unitPrice= #{unitPrice}, total= #{total} where id = #{id}")
    public void updateOrderItemData(OrderItemData orderItemData);

    @Select("SELECT * FROM `order_item` WHERE id=#{id}")
    public OrderItemData getOrderItem(int id);

    @Select("SELECT * FROM `order_item` WHERE orderId=#{orderId}")
    public List<OrderItemData> getOrderItemList(String orderId);



    @Insert("INSERT INTO order_service(orderId,servicerId,servicerName,createTime,commentStatus,scoreService,scoreProfess,scoreResponse,commentText,commentUserId,commentTime)VALUES(#{orderId},#{servicerId},#{servicerName},#{createTime},#{commentStatus},scoreService=#{scoreService},scoreProfess=#{scoreProfess},scoreResponse=#{scoreResponse},commentText =#{commentText},commentUserId=#{commentUserId},commentTime=#{commentTime})")
    public void addOrderService(OrderServiceData serviceData);


    @Select("SELECT * FROM order_service WHERE orderId=#{orderId}")
    public OrderServiceData getOrderService(String orderId);

    @Update("UPDATE order_service SET commentStatus=#{commentStatus},scoreService=#{scoreService},scoreProfess=#{scoreProfess},scoreResponse=#{scoreResponse},commentText=#{commentText},commentUserId=#{commentUserId},commentTime=#{commentTime} WHERE orderId = #{orderId}")
    public void updateOrderServiceData(OrderServiceData serviceData);


    @Select("SELECT (AVG(scoreService) +AVG(scoreProfess)+AVG(scoreResponse)) AS totalScore FROM order_service WHERE servicerId=#{servicerId}")
    public Integer sumTotalScore(int servicerId);
}