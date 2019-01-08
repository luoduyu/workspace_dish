package com.amt.wechat.form.order;

import com.alibaba.fastjson.JSON;
import com.amt.wechat.model.order.MyOrderForm;
import com.amt.wechat.model.order.OrderServiceData;

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
     * 优惠券扣减金额,单位:分
     */
    private int couponPaid;

    /**
     * 余额扣减金额,单位:分
     */
    private int balancePaid;

    /**
     * 微信扣减金额,单位:分
     */
    private int wechatPaid;


    /**
     * 订单支付时间
     */
    private String payTime;

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

    public int getCouponPaid() {
        return couponPaid;
    }

    public void setCouponPaid(int couponPaid) {
        this.couponPaid = couponPaid;
    }

    public int getBalancePaid() {
        return balancePaid;
    }

    public void setBalancePaid(int balancePaid) {
        this.balancePaid = balancePaid;
    }

    public int getWechatPaid() {
        return wechatPaid;
    }

    public void setWechatPaid(int wechatPaid) {
        this.wechatPaid = wechatPaid;
    }

    public OrderServiceData getOrderServiceData() {
        return orderServiceData;
    }

    public void setOrderServiceData(OrderServiceData orderServiceData) {
        this.orderServiceData = orderServiceData;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}