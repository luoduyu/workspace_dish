package com.wmt.mgr.model.balance;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * 账户余额消费记录
 * @author lujunp Create on 2019/2/28 14:53
 * @version 1.0
 */
@Alias("balanceConsumeData")
public class BalanceConsumeData implements Serializable {

    private static final long serialVersionUID = -5428223092582403910L;

    /**
     * id
     */
    private int id;
    /**
     * 店铺Id
     */
    private String poiId;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 操作人Id
     */
    private String userId;
    /**
     * 操作人姓名
     */
    private String userName;

    /**
     * 订单号
     */
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
        return super.toString();
    }
}
