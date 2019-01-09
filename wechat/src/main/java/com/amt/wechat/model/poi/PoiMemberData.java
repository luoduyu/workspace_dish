package com.amt.wechat.model.poi;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *  店铺会员信息
 *
 * @author adu Create on 2019-01-09 19:29
 * @version 1.0
 */
public class PoiMemberData implements Serializable {
    private static final long serialVersionUID = -1885578530009761558L;

    /**
     * 店铺Id
     */
    private String poiId;


    /**
     * 会员卡时长,单位:天
     */
    private int durationDays;

    /**
     * 会员卡购买花费,单位:分
     */
    private int cost;

    /**
     * 会员卡购买时间
     */
    private String buyTime;

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }


    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }

    @Override
    public String toString() {
        return "PoiMemberData{" +
                "poiId='" + poiId + '\'' +
                ", durationDays=" + durationDays +
                ", cost=" + cost +
                ", buyTime='" + buyTime + '\'' +
                '}';
    }
}