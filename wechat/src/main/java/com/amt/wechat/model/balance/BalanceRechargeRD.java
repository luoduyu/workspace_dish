package com.amt.wechat.model.balance;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  余额充值记录
 *
 * @author adu Create on 2019-01-12 11:26
 * @version 1.0
 */
public class BalanceRechargeRD implements Serializable {
    private static final long serialVersionUID = 5552500221517818154L;

    private int id;
    private String poiId;
    private String userId;
    private String userName;
    private int amount;
    private String rechargeNo;
    private String createTime;
    private int balance;


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

    public String getRechargeNo() {
        return rechargeNo;
    }

    public void setRechargeNo(String rechargeNo) {
        this.rechargeNo = rechargeNo;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "BalanceRechargeRD{" +
                "id=" + id +
                ", poiId='" + poiId + '\'' +
                ", userId='" + userId + '\'' +
                ", amount=" + amount +
                ", rechargeNo='" + rechargeNo + '\'' +
                ", createTime='" + createTime + '\'' +
                ", balance=" + balance +
                '}';
    }
}