package com.amt.wechat.model.bidding;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *  充值记录
 *
 * @author adu Create on 2019-01-08 17:19
 * @version 1.0
 */
public class BiddingRechargeRd implements Serializable {
    private static final long serialVersionUID = -1928443080034978131L;

    private int id;
    private String poiId;
    private String userId;
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

    @Override
    public String toString() {
        return "BiddingRechargeRd{" +
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