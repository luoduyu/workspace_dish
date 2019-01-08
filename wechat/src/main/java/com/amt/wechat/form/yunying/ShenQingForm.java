package com.amt.wechat.form.yunying;

import java.io.Serializable;

/**
 * Copyright (c) 2018 by CANSHU
 *
 *  开店/运营申请
 *
 * @author adu Create on 2018-12-29 16:43
 * @version 1.0
 */
public class ShenQingForm implements Serializable {
    private static final long serialVersionUID = 6014201649360779089L;

    /**
     * 0:开店申请;1:f运营申请
     */
    private int usefor;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区
     */
    private String districts;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 已上线餐卖平台;空字串表示无;1:美团;2:饿了么;"1,2"表示美团饿了么二者兼备
     */
    private String platform;

    /**
     *
     * 门店类型;1:单店自创品牌;2:连锁加盟;3:连锁直营
     */
    private int poiType;

    /**
     * 经营品类Id
     */
    private int dishCateId;

    /**
     * 门店数量
     */
    private int amount;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人手机
     */
    private String contactMobile;


    private String smsCode;

    public int getUsefor() {
        return usefor;
    }

    public void setUsefor(int usefor) {
        this.usefor = usefor;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getPoiType() {
        return poiType;
    }

    public void setPoiType(int poiType) {
        this.poiType = poiType;
    }

    public int getDishCateId() {
        return dishCateId;
    }

    public void setDishCateId(int dishCateId) {
        this.dishCateId = dishCateId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }



    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    @Override
    public String toString() {
        return "ShenQingForm{" +
                ", brandName='" + brandName + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", districts='" + districts + '\'' +
                ", address='" + address + '\'' +
                ", platform=" + platform +
                ", poiType=" + poiType +
                ", dishCateId=" + dishCateId +
                ", amount=" + amount +
                ", contactName='" + contactName + '\'' +
                ", contactMobile='" + contactMobile + '\'' +
                '}';
    }
}