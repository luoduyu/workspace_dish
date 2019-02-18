package com.amt.wechat.model.bidding;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  竞价帐户消费记录
 * @author adu Create on 2019-01-08 17:39
 * @version 1.0
 */
public class BiddingConsumeRd implements Serializable {
    private static final long serialVersionUID = -5552587796122335076L;

    private int id;
    private String poiId;
    private String servicerId;
    private String servicerName;
    private String platform;
    private int amount;
    private String consumeNo;
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

    public String getServicerId() {
        return servicerId;
    }

    public void setServicerId(String servicerId) {
        this.servicerId = servicerId;
    }

    public String getServicerName() {
        return servicerName;
    }

    public void setServicerName(String servicerName) {
        this.servicerName = servicerName;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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

    @Override
    public String toString() {
        return "BiddingConsumeRd{" +
                "id=" + id +
                ", poiId='" + poiId + '\'' +
                ", servicerId='" + servicerId + '\'' +
                ", servicerName='" + servicerName + '\'' +
                ", platform='" + platform + '\'' +
                ", amount=" + amount +
                ", consumeNo='" + consumeNo + '\'' +
                ", createTime='" + createTime + '\'' +
                ", balance=" + balance +
                '}';
    }
}