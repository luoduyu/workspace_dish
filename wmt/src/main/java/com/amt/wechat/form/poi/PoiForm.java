package com.amt.wechat.form.poi;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-31 13:44
 * @version 1.0
 */
public class PoiForm implements Serializable {
    private static final long serialVersionUID = -3109298353758602969L;

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
     * 图片地址
     */
    private String logoImg;


    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 是否有设置余额支付密码;false:没有设置,true:有设置
     */
    private boolean isBalancePwdSet = false;


    /**
     * 是否余额免密支付,0:否,1:是
     */
    private int balancePwdFree=0;

    /**
     * 当前帐户余额,单位:分
     */
    private int curBalance;



    /**
     * 当前红包帐户余额,单位:分
     */
    private int curRedBalance;

    /**
     * 当前竞价充值余额,单位:分
     */
    private int curBiddingBalance;


    /**
     * 是否完成了美团授权认证
     */
    private boolean mtAuth = false;

    /**
     * 是否完成了饿了么授权认证
     */
    private boolean eleAuth = false;


    /**
     * 店主手机号
     */
    private String masterMobile;


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

    public String getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(String logoImg) {
        this.logoImg = logoImg;
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

    public int getBalancePwdFree() {
        return balancePwdFree;
    }

    public void setBalancePwdFree(int balancePwdFree) {
        this.balancePwdFree = balancePwdFree;
    }

    public int getCurBalance() {
        return curBalance;
    }

    public void setCurBalance(int curBalance) {
        this.curBalance = curBalance;
    }

    public int getCurRedBalance() {
        return curRedBalance;
    }

    public void setCurRedBalance(int curRedBalance) {
        this.curRedBalance = curRedBalance;
    }

    public int getCurBiddingBalance() {
        return curBiddingBalance;
    }

    public void setCurBiddingBalance(int curBiddingBalance) {
        this.curBiddingBalance = curBiddingBalance;
    }


    public boolean isMtAuth() {
        return mtAuth;
    }

    public void setMtAuth(boolean mtAuth) {
        this.mtAuth = mtAuth;
    }

    public boolean isEleAuth() {
        return eleAuth;
    }

    public void setEleAuth(boolean eleAuth) {
        this.eleAuth = eleAuth;
    }

    public String getMasterMobile() {
        return masterMobile;
    }

    public void setMasterMobile(String masterMobile) {
        this.masterMobile = masterMobile;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
