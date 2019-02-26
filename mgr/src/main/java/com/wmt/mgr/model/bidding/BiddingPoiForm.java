package com.wmt.mgr.model.bidding;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-26 14:38
 * @version 1.0
 */
public class BiddingPoiForm implements Serializable {
    private static final long serialVersionUID = 753562008189777993L;


    /**
     * 门店Id
     */
    private String id;

    /**
     * 品牌名称
     */
    private String brandName;

    private String masterName;

    private String masterMobile;

    /**
     * 当前竞价充值余额,单位:分
     */
    private int curBiddingBalance;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public String getMasterMobile() {
        return masterMobile;
    }

    public void setMasterMobile(String masterMobile) {
        this.masterMobile = masterMobile;
    }

    public int getCurBiddingBalance() {
        return curBiddingBalance;
    }

    public void setCurBiddingBalance(int curBiddingBalance) {
        this.curBiddingBalance = curBiddingBalance;
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
