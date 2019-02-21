package com.wmt.wechat.model.order;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 * 我的订单
 *
 * @author adu Create on 2019-01-05 15:20
 *
 * @version 1.0
 */
@Alias("myOrderForm")
public class MyOrderForm implements Serializable {

    private static final long serialVersionUID = -871507762047965548L;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 物品类型;1:海报;2:装修服务;3:抢购物品
     */
    private int goodsType;

    /**
     * 订单创建时间
     */
    private String createTime;

    /**
     * 支付状态;1:待付款;2:已付款;
     */
    private int payStatus;


    /**
     * 实付金额;单位:分;
     */
    private int payment;


    /**
     * 服务状态;0:未处理;1:处理中;2:已完成
     */
    private int serviceStatus;


    /**
     * 订单明细
     */
    private List<OrderItemData> itemList;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public List<OrderItemData> getItemList() {
        return itemList;
    }

    public void setItemList(List<OrderItemData> itemList) {
        this.itemList = itemList;
    }

    public int getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(int serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}