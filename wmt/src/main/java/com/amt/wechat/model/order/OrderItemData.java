package com.amt.wechat.model.order;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  订单-物品项
 *
 * @author adu Create on 2019-01-04 19:02
 * @version 1.0
 */
@Alias("orderItemData")
public class OrderItemData implements Serializable {
    private static final long serialVersionUID = 8599317807676704847L;

    private int id;

    /**
     * 1:海报;2:装修服务
     */
    private int goodsType;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 海报Id/装修服务项Id
     */
    private int goodsId;


    /**
     * 抢购序列
     */
    private Long snapSeq;

    /**
     * 海服名称/服务项名称
     */
    private String goodsName;

    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     * 购买数量
     */
    private int num;

    /**
     * 单价,单位:分
     */
    private int unitPrice;

    /**
     * 总金额,单位:分
     */
    private int total;


    /**
     * 订单创建时间
     */
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public Long getSnapSeq() {
        return snapSeq;
    }

    public void setSnapSeq(Long snapSeq) {
        this.snapSeq = snapSeq;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }
}