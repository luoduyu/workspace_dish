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
    private String createTime;
    private String userId;
    private String userName;

    private String orderId;

    /**
     * 消费类别
     */
    private int cateId;

    /**
     * 消费金额 =正常余额+ 红包金额
     */
    private int amount;

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


    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "BalanceConsumeRd{" +
                "id=" + id +
                ", poiId='" + poiId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", orderId='" + orderId + '\'' +
                ", cateId=" + cateId +
                ", summary='" + summary + '\'' +
                '}';
    }
}