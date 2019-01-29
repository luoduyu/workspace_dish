package com.amt.wechat.model.snap;

import java.io.Serializable;

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

    private Integer goodsId;

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

    @Override
    public String toString() {
        return "SnapGoodsTemplateData{" +
                ", name='" + name + '\'' +
                ", coverImg='" + coverImg + '\'' +
                ", unitName='" + unitName + '\'' +
                '}';
    }
}