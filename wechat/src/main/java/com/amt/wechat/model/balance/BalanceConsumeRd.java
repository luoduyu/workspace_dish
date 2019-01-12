package com.amt.wechat.model.balance;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  余额帐户消费记录
 *
 * @author adu Create on 2019-01-08 17:39
 * @version 1.0
 */
public class BalanceConsumeRd implements Serializable {
    private static final long serialVersionUID = -5552587796122335076L;

    private int id;
    private String poiId;
    private int amount;
    private String consumeNo;
    private String createTime;
    private int balance;

    private String userId;
    private String userName;

    /**
     * 接要
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


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getConsumeNo() {
        return consumeNo;
    }

    public void setConsumeNo(String consumeNo) {
        this.consumeNo = consumeNo;
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


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "BalanceConsumeRd{" +
                "id=" + id +
                ", poiId='" + poiId + '\'' +
                ", amount=" + amount +
                ", consumeNo='" + consumeNo + '\'' +
                ", createTime='" + createTime + '\'' +
                ", balance=" + balance +
                '}';
    }
}