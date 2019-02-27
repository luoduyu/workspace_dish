package com.wmt.mgr.model.member;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 * 店铺会员购买记录
 *
 * @author lujunp Create on 2019/2/27 12:25
 * @version 1.0
 */
@Alias("poiMemberRDData")
public class PoiMemberRDData implements Serializable {

    private static final long serialVersionUID = 9221788232229080756L;
    /**
     * 订单Id
     */
    private long id;

    /**
     * 店铺Id
     */
    private String poiId;

    /**
     * 店铺名称
     */
    private String poiName;
    /**
     * 会员卡时长单位
     */
    private String durationUnit;

    /**
     * 会员卡时长
     */
    private int duration;

    /**
     * 购买日期
     */
    private String buyTime;

    /**
     * 买主Id(userId)
     */
    private String userId;

    /**
     * 买主手机
     */
    private String userMobile;

    /**
     * 总价,单位:分
     */
    private int total;

    /**
     * 折扣额度,单位:分
     */
    private int discount;

    /**
     * 实付,单位:分
     */
    private int payment;
    /**
     * 支付途径,1:银联,2:支付宝,3:微信,4:余额支付
     */
    private int payWay;
    /**
     * 付状态;1:待付款;2:已付款;
     */
    private int payStatus;

    /**
     * 支付完成时间，格式为yyyy-MM-dd HH:mm:ss
     */
    private String timeEnd;

    /**
     * 支付单号
     */
    private String orderId;

    /**
     * 是否自动续费:0:否,1:是
     */
    private int feeRenew;

    /**
     * 微信支付单号
     */
    private String transactionId;

    /**
     * 摘要
     */
    private String summary;

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

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
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

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getFeeRenew() {
        return feeRenew;
    }

    public void setFeeRenew(int feeRenew) {
        this.feeRenew = feeRenew;
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
