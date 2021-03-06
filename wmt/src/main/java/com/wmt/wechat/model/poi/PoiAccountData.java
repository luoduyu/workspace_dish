package com.wmt.wechat.model.poi;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  店铺帐户
 *
 * @author adu Create on 2019-01-08 19:02
 * @version 1.0
 */
public class PoiAccountData implements Serializable {
    private static final long serialVersionUID = -1416441025244934010L;

    private String poiId;

    /**
     * 当前帐户余额,单位:分
     */
    private int curBalance;
    private int curRedBalance;

    /**
     * 当前竞价充值余额,单位:分
     */
    private int curBiddingBalance;


    /**
     * 节省花费,单位:分
     */
    private int costSave;



    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public int getCurBalance() {
        return curBalance;
    }

    public void setCurBalance(int curBalance) {
        this.curBalance = curBalance;
    }

    public int getCurRedBalance() {
        return curRedBalance;
    }

    public void setCurRedBalance(int curRedBalance) {
        this.curRedBalance = curRedBalance;
    }

    public int getCurBiddingBalance() {
        return curBiddingBalance;
    }

    public void setCurBiddingBalance(int curBiddingBalance) {
        this.curBiddingBalance = curBiddingBalance;
    }

    public int getCostSave() {
        return costSave;
    }

    public void setCostSave(int costSave) {
        this.costSave = costSave;
    }

    @Override
    public String toString() {
        return "PoiAccountData{" +
                "poiId='" + poiId + '\'' +
                ", curBalance=" + curBalance +
                ", curRedBalance=" + curRedBalance +
                ", curBiddingBalance=" + curBiddingBalance +
                '}';
    }
}