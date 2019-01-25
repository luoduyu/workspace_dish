package com.amt.wechat.model.balance;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  充值金额档位
 *
 * @author adu Create on 2019-01-08 17:09
 * @version 1.0
 */
public class CurrencyStageData implements Serializable {
    private static final long serialVersionUID = -5440755507476099068L;

    private int id;
    private int cost;

    /**
     * 赠送红包数额,单位:分
     */
    private int red;

    /**
     * 显示顺序,从小大小
     */
    private int showSeq;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getShowSeq() {
        return showSeq;
    }

    public void setShowSeq(int showSeq) {
        this.showSeq = showSeq;
    }

    @Override
    public String toString() {
        return "CurrencyStageData{" +
                "id=" + id +
                ", cost=" + cost +
                ", red=" + red +
                ", showSeq=" + showSeq +
                '}';
    }
}