package com.wmt.mgr.model.balance;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 * 商户账户余额充值记录
 *
 * @author lujunp Create on 2019/2/28 15:25
 * @version 1.0
 */
@Alias("balanceRechargeData")
public class BalanceRechargeData implements Serializable {

    private static final long serialVersionUID = 8808457777732401324L;

    /**
     * id
     */
    private int id;
    /**
     * 店铺Id
     */
    private String poiId;
    /**
     * 充值人Id
     */
    private String userId;
    /**
     * 充值人姓名
     */
    private String userName;
    /**
     * 实付,单位:分
     */
    private int amount;
    /**
     * 充值单号
     */
    private String orderId;
    /**
     * 充值日期
     */
    private String createTime;
    /**
     * 充值余额
     */
    private int balance;
    /**
     * 红包充值余额
     */
    private int redBalance;
    /**
     * 支付途径,1:银联,2:支付宝,3:微信
     */
    private Integer payWay=3;

    /**
     * 支付状态;1:待付款;2:已付款;
     */
    private int payStatus;

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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }


    public int getRedBalance() {
        return redBalance;
    }

    public void setRedBalance(int redBalance) {
        this.redBalance = redBalance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
