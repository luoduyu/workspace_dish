package com.amt.wechat.model.snap;

import java.io.Serializable;

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
     * 抢购序列
     */
    private Long seq;

    /**
     * 所属分类Id
     */
    private Integer cateId;


    /**
     * 物品名称
     */
    private String name;

    /**
     * 物品Id
     */
    private Integer goodsId;


    /**
     * 抢购日期
     */
    private String snapDate;

    /**
     * 拾购开始时间
     */
    private String timeFrameStart;

    /**
     * 拾购结束时间
     */
    private String timeFrameEnd;

    /**
     * 抢购价,单位:分
     */
    private Integer disPrice;

    /**
     * 原价,单位:分
     */
    private Integer oriPrice;


    /**
     * 抢购数量
     */
    private Integer snapNumEnable=1;

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }



    public String getSnapDate() {
        return snapDate;
    }

    public void setSnapDate(String snapDate) {
        this.snapDate = snapDate;
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


    public Integer getSnapNumEnable() {
        return snapNumEnable;
    }

    public void setSnapNumEnable(Integer snapNumEnable) {
        this.snapNumEnable = snapNumEnable;
    }

    @Override
    public String toString() {
        return "SnapGoodsData{" +
                "cateId=" + cateId +
                ", goodsId=" + goodsId +
                ", snapDate='" + snapDate + '\'' +
                ", timeFrameStart='" + timeFrameStart + '\'' +
                ", timeFrameEnd='" + timeFrameEnd + '\'' +
                ", disPrice=" + disPrice +
                ", oriPrice=" + oriPrice +
                '}';
    }
}
