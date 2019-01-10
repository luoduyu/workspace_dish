package com.amt.wechat.model.poi;

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
	private String durationUnit;
	private int duration;
	private String buyTime;
	private String userId;
	private int total;

    /**
     * 折扣额度,单位:分
     */
	private int newDiscount;
	private int payment;

    /**
     * 付状态;1:待付款;2:已付款;
     */
	private int payStatus;
	private String payTime;
	private String payNo;

    /**
     * 是否自动续费:0:否,1:是
     */
	private int feeRenew;

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

    public int getNewDiscount() {
        return newDiscount;
    }

    public void setNewDiscount(int newDiscount) {
        this.newDiscount = newDiscount;
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

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public int getFeeRenew() {
        return feeRenew;
    }

    public void setFeeRenew(int feeRenew) {
        this.feeRenew = feeRenew;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}