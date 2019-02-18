package com.amt.wechat.model.order;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *  抢购数量
 *
 * @author adu Create on 2019-01-28 17:01
 * @version 1.0
 */
public class SnapSoldData implements Serializable {
    private static final long serialVersionUID = 3245998087471213915L;


    /**
     * 小时段
     */
    private String hr;


    /**
     * 销售数量
     */
    private int soldNum;



    public SnapSoldData() {
    }

    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }

    public int getSoldNum() {
        return soldNum;
    }

    public void setSoldNum(int soldNum) {
        this.soldNum = soldNum;
    }



    @Override
    public String toString() {
        return "SnapSoldData{" +
                "hr=" + hr +
                ", soldNum=" + soldNum +
                '}';
    }
}
