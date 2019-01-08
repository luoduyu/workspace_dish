package com.amt.wechat.model.poi;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  门店数据
 *
 * @author adu Create on 2019-01-03 17:21
 * @version 1.0
 */
public class PoiData implements Serializable {
    private static final long serialVersionUID = 98370870191036587L;


    /**
     * 门店Id
     */
    private String id;

    /**
     * 门店名称
     */
    private String name;


    /**
     * 0:非会员;1:会员
     */
    private int memberFlag;

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
     * 门店帐户名
     */
    private String accountName;

    /**
     *
     */
    private String accountPassword;

    /**
     * 美团门店id
     */
    private String mtAppAuthToken;

    /**
     * 饿了么门店Id
     */
    private String eleShopId;


    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 最后更新时间
     */
    private String updTime;

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
    public int getMemberFlag() {
        return memberFlag;
    }

    public void setMemberFlag(int memberFlag) {
        this.memberFlag = memberFlag;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public String getMtAppAuthToken() {
        return mtAppAuthToken;
    }

    public void setMtAppAuthToken(String mtAppAuthToken) {
        this.mtAppAuthToken = mtAppAuthToken;
    }

    public String getEleShopId() {
        return eleShopId;
    }

    public void setEleShopId(String eleShopId) {
        this.eleShopId = eleShopId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdTime() {
        return updTime;
    }

    public void setUpdTime(String updTime) {
        this.updTime = updTime;
    }

    @Override
    public String toString() {
        return "PoiData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", accountName='" + accountName + '\'' +
                ", accountPassword='" + accountPassword + '\'' +
                ", mtAppAuthToken='" + mtAppAuthToken + '\'' +
                ", eleShopId='" + eleShopId + '\'' +
                '}';
    }
}