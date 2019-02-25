package com.wmt.wechat.model.poi;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * 店铺会员购买记录
 *
 * @author adu Create on 2019-01-10 16:34
 * @version 1.0
 */
public class PoiMemberRDData implements Serializable {
    private static final long serialVersionUID = -8966842014804947132L;

	private long id;
	private String poiId;
	private String poiName;
	private String durationUnit;
	private int duration;
	private String buyTime;
	private String userId;
    private String userMobile;

	private int total;

    /**
     * 折扣额度,单位:分
     */
	private int discount;

    /**
     * 实付款,单位:分
     */
	private int payment;

    private String orderId;

    /**
     * 付状态;1:待付款;2:已付款;
     */
	private int payStatus;


    /**
     * 是否自动续费:0:否,1:是
     */
	private int feeRenew;

    /**
     * 支付途径,1:银联,2:支付宝,3:微信,4:余额支付
     */
    private Integer payWay=3;



    /**
     * 微信支付订单号
     */
    private String transactionId="";


    /**
     * 支付完成时间
     */
    private String timeEnd="";

    /**
     * 摘要
     */
    private String summary="";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
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
    public int getFeeRenew() {
        return feeRenew;
    }

    public void setFeeRenew(int feeRenew) {
        this.feeRenew = feeRenew;
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

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}