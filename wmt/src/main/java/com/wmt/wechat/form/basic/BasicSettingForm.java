package com.wmt.wechat.form.basic;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *
 * @author adu Create on 2019-01-04 14:38
 * @version 1.0
 */
public class BasicSettingForm implements Serializable {
    private static final long serialVersionUID = 774762321981421296L;


    /**
     * 店主姓名
     */
    private String memberName="";

    /**
     * 品牌名称
     */
    private String poiBrandName;


    /**
     * 经营品类
     */
    private int poiCateId;


    /**
     * 省份
     */
    private String poiProvince;

    /**
     * 市
     */
    private String poiCity;


    /**
     * 区
     */
    private String poiDistricts;

    /**
     * 街道
     */
    private String poiStreet;


    /**
     * 详细地址
     */
    private String poiAddress;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getPoiProvince() {
        return poiProvince;
    }

    public void setPoiProvince(String poiProvince) {
        this.poiProvince = poiProvince;
    }

    public String getPoiCity() {
        return poiCity;
    }

    public void setPoiCity(String poiCity) {
        this.poiCity = poiCity;
    }

    public String getPoiDistricts() {
        return poiDistricts;
    }

    public void setPoiDistricts(String poiDistricts) {
        this.poiDistricts = poiDistricts;
    }

    public String getPoiStreet() {
        return poiStreet;
    }

    public void setPoiStreet(String poiStreet) {
        this.poiStreet = poiStreet;
    }


    public String getPoiAddress() {
        return poiAddress;
    }

    public void setPoiAddress(String poiAddress) {
        this.poiAddress = poiAddress;
    }

    public String getPoiBrandName() {
        return poiBrandName;
    }

    public void setPoiBrandName(String poiBrandName) {
        this.poiBrandName = poiBrandName;
    }

    public int getPoiCateId() {
        return poiCateId;
    }

    public void setPoiCateId(int poiCateId) {
        this.poiCateId = poiCateId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}