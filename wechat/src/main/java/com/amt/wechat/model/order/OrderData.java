package com.amt.wechat.model.order;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  订单
 *
 * @author adu Create on 2019-01-04 19:02
 * @version 1.0
 */
@Alias("orderData")
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
     * 实付金额;单位:分;
     */
    private int payment;


    /**
     * 支付状态;1:待付款;2:已付款;
     */
    private int payStatus;


    /**
     * 订单创建时间
     */
    private String createTime;

    /**
     * 订单支付时间
     */
    private String payTime;

    /**
     * 支付编号
     */
    private String payNo;


    /**
     * 服务状态;0:未处理;1:处理中;2:已完成
     */
    private int serviceStatus;

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

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
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

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public int getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(int serviceStatus) {
        this.serviceStatus = serviceStatus;
    }
    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }
}