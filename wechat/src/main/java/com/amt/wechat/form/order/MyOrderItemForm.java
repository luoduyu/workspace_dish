package com.amt.wechat.form.order;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-08 13:22
 * @version 1.0
 */
public class MyOrderItemForm implements Serializable {
    private static final long serialVersionUID = -526889382882254589L;

    /**
     * id
     */
    private int id;

    /**
     * 物品Id(海报/装修服务项等Id)
     */
    private Integer goodsId;

    /**
     * 数量
     */
    private int num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "MyOrderItemForm{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", num=" + num +
                '}';
    }
}