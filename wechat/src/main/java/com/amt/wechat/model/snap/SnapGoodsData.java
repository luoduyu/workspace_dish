package com.amt.wechat.model.snap;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Date;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * 抢购物品
 *
 * @author adu Create on 2019-01-28 14:51
 * @version 1.0
 */
public class SnapGoodsData implements Serializable {
    private static final long serialVersionUID = -789291313502629104L;

    /**
     * 序列号
     */
    private Long seq;

    /**
     * 抢购日期
     */
    private Date snapDate;

    /**
     * 所属分类Id
     */
    private Integer cateId;

    /**
     * 拾购开始时间
     */
    private String timeFrameStart;

    /**
     * 拾购结束时间
     */
    private String timeFrameEnd;

    /**
     * 所属时段库存量
     */
    private Integer timeFrameStockNum;

    /**
     * 物品Id
     */
    private Integer goodsId;

    /**
     * 物品名称
     */
    private String name;

    /**
     * 物品单位名称
     */
    private String unitName;

    /**
     * 图片地址
     */
    private String coverImg;

    /**
     * 抢购数量
     */
    private Integer snapNumEnable;

    /**
     * 抢购价,单位:分
     */
    private Integer disPrice;

    /**
     * 原价,单位:分
     */
    private Integer oriPrice;

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Date getSnapDate() {
        return snapDate;
    }

    public void setSnapDate(Date snapDate) {
        this.snapDate = snapDate;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public String getTimeFrameStart() {
        return timeFrameStart;
    }

    public void setTimeFrameStart(String timeFrameStart) {
        this.timeFrameStart = timeFrameStart;
    }

    public String getTimeFrameEnd() {
        return timeFrameEnd;
    }

    public void setTimeFrameEnd(String timeFrameEnd) {
        this.timeFrameEnd = timeFrameEnd;
    }

    public Integer getTimeFrameStockNum() {
        return timeFrameStockNum;
    }

    public void setTimeFrameStockNum(Integer timeFrameStockNum) {
        this.timeFrameStockNum = timeFrameStockNum;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public Integer getSnapNumEnable() {
        return snapNumEnable;
    }

    public void setSnapNumEnable(Integer snapNumEnable) {
        this.snapNumEnable = snapNumEnable;
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
