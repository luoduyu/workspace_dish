package com.amt.wechat.model.snap;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Date;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *   抢购物品生成的模板
 *
 * @author adu Create on 2019-01-28 14:50
 * @version 1.0
 */
public class SnapGoodsTemplateData implements Serializable {
    private static final long serialVersionUID = 159193267058543523L;

    /**
     * 物品Id
     */
    private Integer goodsId;

    /**
     * 所属分类
     */
    private Integer cateId;

    /**
     * 抢购数据Id
     */
    private Integer timeFrameId;

    /**
     * 物品名称
     */
    private String name;

    /**
     * 图片名称
     */
    private String coverImg;

    /**
     * 优惠价,单位:分
     */
    private Integer disPrice;

    /**
     * 原价,单位:分
     */
    private Integer oriPrice;

    /**
     * 每单可抢购数量
     */
    private Integer snapNumEnable;

    /**
     * 单位,默认'项'
     */
    private String unitName;

    /**
     * 开始日期限制
     */
    private Date dateStart;

    /**
     * 结束日期限制
     */
    private Date dateEnd;

    /**
     * 星期限制,0代表周一,5表示周六,6表示周日
     */
    private String dayOfWeek;


    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getCateId() {
        return cateId;
    }

    public void setCateId(Integer cateId) {
        this.cateId = cateId;
    }

    public Integer getTimeFrameId() {
        return timeFrameId;
    }

    public void setTimeFrameId(Integer timeFrameId) {
        this.timeFrameId = timeFrameId;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}