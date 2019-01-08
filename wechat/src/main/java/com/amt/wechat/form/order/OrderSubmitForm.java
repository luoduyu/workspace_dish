package com.amt.wechat.form.order;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * 订单提交用Form
 *
 * @author adu Create on 2019-01-08 13:18
 * @version 1.0
 */
public class OrderSubmitForm implements Serializable {
    private static final long serialVersionUID = -8610278094573590479L;


    /**
     * 1:海报;2:装修服务
     */
    private int goodsType;


    private List<MyOrderItemForm> orderItemList;

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public List<MyOrderItemForm> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<MyOrderItemForm> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}