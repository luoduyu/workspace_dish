package com.wmt.wechat.form.order;

import com.alibaba.fastjson.JSON;
import com.wmt.wechat.model.order.MyOrderForm;
import com.wmt.wechat.model.order.OrderServiceData;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-05 18:27
 * @version 1.0
 */
public class MyOrderFormEx extends MyOrderForm implements Serializable {
    private static final long serialVersionUID = 5691039506415940311L;

    /**
     * 商品总价,单位:分
     */
    private int total;


    /**
     * 订单支付时间
     */
    private String timeEnd;


    /**
     * 支付途径,1:银联,2:支付宝,3:微信,4:余额支付
     */
    private Integer payWay=3;


    /**
     * 微信支付订单号
     */
    private String transactionId="";


    /**
     * 摘要
     */
    private String summary="";

    /**
     * 服务评价
     */
    private OrderServiceData orderServiceData;


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public OrderServiceData getOrderServiceData() {
        return orderServiceData;
    }

    public void setOrderServiceData(OrderServiceData orderServiceData) {
        this.orderServiceData = orderServiceData;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}