package com.wmt.wechat.dao.order;

import com.wmt.wechat.model.order.MyOrderForm;
import com.wmt.wechat.model.order.OrderData;
import com.wmt.wechat.model.order.OrderItemData;
import com.wmt.wechat.model.order.OrderServiceData;
import com.wmt.wechat.model.order.SnapSoldData;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-04 19:20
 * @version 1.0
 */
@Repository("orderDao")
@Mapper
public interface OrderDao {

    @Insert("INSERT INTO `order`(poiId,poiName,orderId,goodsType,total,payWay,payment,payStatus,createTime,timeEnd,transactionId,summary,serviceStatus,submitUserId,submitUserMobile,submitUserName,payUserId) VALUES(#{poiId},#{poiName},#{orderId},#{goodsType},#{total},#{payWay},#{payment},#{payStatus},#{createTime},#{timeEnd},#{transactionId},#{summary},#{serviceStatus},#{submitUserId},#{submitUserMobile},#{submitUserName},#{payUserId})")
    public void addOrderData(OrderData orderData);

    @Select("SELECT * FROM `order` WHERE orderId=#{orderId}")
    public OrderData getOrder(String orderId);

    @Delete("DELETE FROM `order` WHERE orderId=#{orderId}")
    public void removeOrder(String orderId);


    @Update("UPDATE `order` SET total =#{total} WHERE orderId=#{id}")
    public void updateTotal(int total,String id);

    @Update("UPDATE `order` SET payWay=#{orderData.payWay},payStatus=#{orderData.payStatus},transactionId=#{orderData.transactionId},timeEnd=#{orderData.timeEnd},summary=#{orderData.summary},payUserId=#{orderData.payUserId} WHERE orderId = #{orderData.orderId} AND payStatus=#{oldPayStatus}")
    public void updateOrderData(@Param("orderData") OrderData orderData,@Param("oldPayStatus") int oldPayStatus);




    public List<MyOrderForm> getMyOrderList(String poiId, int index, int pageSize);

    @Select("SELECT COUNT(*) FROM `order` WHERE poiId=#{poiId} AND ((goodsType=1 OR goodsType=2) OR (goodsType=3 AND  payStatus=2))")
    public int countMyOrder(String poiId);


    @Delete("DELETE FROM order_item WHERE orderId=#{orderId}")
    public void removeOrderItemByOrderId(String orderId);






    @Insert("INSERT INTO order_item (orderId,goodsType,goodsId,goodsName,imgUrl,num,unitPrice,total,snapSeq,createTime) VALUES(#{orderId},#{goodsType},#{goodsId},#{goodsName},#{imgUrl},#{num},#{unitPrice},#{total},#{snapSeq},#{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int addOrderItemData(OrderItemData orderItemData);


    public void addOrderItemDataList(@Param("orderItemDataList") List<OrderItemData> orderItemDataList);

    @Delete("DELETE FROM order_item WHERE id=#{id}")
    public void deleteOrderItemData(int id);

    @Update("UPDATE order_item SET num = #{num}, total= #{total} where id = #{id}")
    public void updateOrderItemData(OrderItemData orderItemData);

    @Select("SELECT * FROM `order_item` WHERE id=#{id}")
    public OrderItemData getOrderItem(int id);

    @Select("SELECT * FROM `order_item` WHERE orderId=#{orderId}")
    public List<OrderItemData> getOrderItemList(String orderId);

    @Select("SELECT * FROM `order_item` WHERE orderId=#{orderId} AND goodsType=#{goodsType} AND goodsId=#{goodsId}")
    public OrderItemData getMyOrderItem(String orderId,int goodsType,Integer goodsId);



    @Insert("INSERT INTO order_service(orderId,servicerId,servicerName,createTime,commentStatus,scoreService,scoreProfess,scoreResponse,commentText,commentUserId,commentTime)VALUES(#{orderId},#{servicerId},#{servicerName},#{createTime},#{commentStatus},scoreService=#{scoreService},scoreProfess=#{scoreProfess},scoreResponse=#{scoreResponse},commentText =#{commentText},commentUserId=#{commentUserId},commentTime=#{commentTime})")
    public void addOrderService(OrderServiceData serviceData);


    @Select("SELECT * FROM order_service WHERE orderId=#{orderId}")
    public OrderServiceData getOrderService(String orderId);

    @Update("UPDATE order_service SET commentStatus=#{commentStatus},scoreService=#{scoreService},scoreProfess=#{scoreProfess},scoreResponse=#{scoreResponse},commentText=#{commentText},commentUserId=#{commentUserId},commentTime=#{commentTime} WHERE orderId = #{orderId}")
    public void updateOrderServiceData(OrderServiceData serviceData);


    @Select("SELECT (AVG(scoreService) +AVG(scoreProfess)+AVG(scoreResponse)) AS totalScore FROM order_service WHERE servicerId=#{servicerId}")
    public Integer sumTotalScore(int servicerId);



    @Select("SELECT SUM(num) AS soldNum ,LEFT(TIME(`createTime`),2) AS hr FROM order_item WHERE DATE(`createTime`) = #{todayDate} AND goodsType =3 GROUP BY hr")
    @MapKey("hr")
    public Map<String, SnapSoldData> getSnapSoldMap(String todayDate);
}