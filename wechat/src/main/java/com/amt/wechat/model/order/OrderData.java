package com.amt.wechat.model.order;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  订单
 *
 * @author adu Create on 2019-01-04 19:02
 * @version 1.0
 */
public class OrderData implements Serializable {
    private static final long serialVersionUID = 7130372310462400335L;


    /**
     * 店铺Id
     */
    private String poiId;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 物品类型;1:海报;2:装修服务
     */
    private int goodsType;

    /**
     * 实付金额;单位:分;
     */
    private int payment;

    /**
     * 支付类型;1:在线支付，2:货到付款
     */
    private int paymentType;

    /**
     * 支付渠道;1:微信支付;2:支付宝支付;银联支付
     */
    private int paymentChannel;

    /**
     * 支付状态;1:待付款;2:已付款;
     */
    private int payStatus;


    /**
     * 订单创建时间
     */
    private String createTime;

    /**
     * 订单更新时间
     */
    private String updTime;

    /**
     * 物流单号
     */
    private String shippingCode;

    /**
     * 物品相关摘要
     */
    private String goodsSummary;

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

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

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public int getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(int paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdTime() {
        return updTime;
    }

    public void setUpdTime(String updTime) {
        this.updTime = updTime;
    }

    public String getShippingCode() {
        return shippingCode;
    }

    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode;
    }

    public String getGoodsSummary() {
        return goodsSummary;
    }

    public void setGoodsSummary(String goodsSummary) {
        this.goodsSummary = goodsSummary;
    }

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }
}