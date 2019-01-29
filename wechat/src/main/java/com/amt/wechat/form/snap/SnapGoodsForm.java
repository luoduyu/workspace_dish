package com.amt.wechat.form.snap;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  抢购物品
 *
 * @author adu Create on 2019-01-28 15:08
 * @version 1.0
 */
public class SnapGoodsForm implements Serializable {
    private static final long serialVersionUID = -1314555628121458079L;


    /**
     * 抢购序列
     */
    private Long snapSeq;


    /**
     * 物品Id
     */
    private Integer goodsId;


    /**
     * 可抢购数量
     */
    private int snapNumEnable;

    /**
     * 已售数量
     */
    private int soldNum;


    /**
     * 库存量
     */
    private int stockNum;


    /**
     * 抢购价,单位:分
     */
    private Integer disPrice;

    /**
     * 原价,单位:分
     */
    private Integer oriPrice;

    /**
     * 物品名称
     */
    private String name;

    /**
     * 图片名称
     */
    private String coverImg;


    /**
     * 单位,默认'项'
     */
    private String unitName;

    public Long getSnapSeq() {
        return snapSeq;
    }

    public void setSnapSeq(Long snapSeq) {
        this.snapSeq = snapSeq;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }


    public Integer getDisPrice() {
        return disPrice;
    }

    public void setDisPrice(Integer disPrice) {
        this.disPrice = disPrice;
    }

    public Integer getOriPrice() {
        return oriPrice;
    }

    public void setOriPrice(Integer oriPrice) {
        this.oriPrice = oriPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getSnapNumEnable() {
        return snapNumEnable;
    }

    public void setSnapNumEnable(int snapNumEnable) {
        this.snapNumEnable = snapNumEnable;
    }

    public int getSoldNum() {
        return soldNum;
    }

    public void setSoldNum(int soldNum) {
        this.soldNum = soldNum;
    }


    public int getStockNum() {
        return stockNum;
    }

    public void setStockNum(int stockNum) {
        this.stockNum = stockNum;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}