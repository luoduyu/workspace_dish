package com.amt.wechat.model.poi;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  店铺基本信息
 * @author adu Create on 2019-01-09 16:34
 * @version 1.0
 */
public class PoiBasicData implements Serializable {
    private static final long serialVersionUID = -949676706782292258L;


    /**
     * 门店Id
     */
    private String id;

    /**
     * 门店名称
     */
    private String name;

    /**
     * 国家
     */
    private String country="中国";

    /**
     * 省份
     */
    private String province;

    /**
     * 市
     */
    private String city;


    /**
     * 区
     */
    private String districts;

    /**
     * 街道
     */
    private String street;


    /**
     * 详细地址
     */
    private String address;


    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 经营品类
     */
    private int cateId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 是否有设置余额支付密码;false:没有设置,true:有设置
     */
    private boolean isBalancePwdSet = false;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistricts() {
        return districts;
    }

    public void setDistricts(String districts) {
        this.districts = districts;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isBalancePwdSet() {
        return isBalancePwdSet;
    }

    public void setBalancePwdSet(boolean balancePwdSet) {
        isBalancePwdSet = balancePwdSet;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}