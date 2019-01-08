package com.amt.wechat.form.order;

import com.amt.wechat.model.order.OrderItemData;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * 编辑订单用form
 *
 * @author adu Create on 2019-01-07 17:03
 * @version 1.0
 */
public class OrderItemForm implements Serializable {
    private static final long serialVersionUID = 8772614823680387172L;

    private String orderId;
    private List<MyOrderItemForm> orderItemList;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<MyOrderItemForm> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<MyOrderItemForm> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public List<OrderItemData> let(List<OrderItemData> itemList){
        Map<Integer,MyOrderItemForm> map = new HashMap<>(this.orderItemList.size());
        for(MyOrderItemForm o:orderItemList){
            map.put(o.getId(),o);
        }

        for(OrderItemData o:itemList){
            MyOrderItemForm my = map.get(o.getId());

            // 有发现非法的直接返回null!
            if(my.getGoodsId() != o.getGoodsId()){
                return null;
            }
            if(my.getNum() <= 0){
                return null;
            }
            o.setNum(my.getNum());
        }
        return itemList;
    }

    public boolean validate(){
        if(StringUtils.isEmpty(orderId)){
            return false;
        }
        if(orderItemList == null || orderItemList.isEmpty()){
            return false;
        }
        return true;
    }
}