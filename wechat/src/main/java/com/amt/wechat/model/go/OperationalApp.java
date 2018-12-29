package com.amt.wechat.model.go;

import java.io.Serializable;

/**
 * Copyright (c) 2018 by CANSHU
 *
 *  运营申请
 *
 * @author adu Create on 2018-12-29 16:43
 * @version 1.0
 */
public class OperationalApp implements Serializable {
    private static final long serialVersionUID = 6014201649360779089L;

    private int id;
	private String brandName;
	private String province;
	private String city;
	private String districts;
	private String address;
	private int platform;
	private int poiType;
    private int dishCateId;
    private int amount;
    private String contactName;
    private String contactMobile;
    private String commitDate;
    private String progress;
    private String  poiUserId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
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

    public String getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(String commitDate) {
        this.commitDate = commitDate;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getPoiUserId() {
        return poiUserId;
    }

    public void setPoiUserId(String poiUserId) {
        this.poiUserId = poiUserId;
    }

    @Override
    public String toString() {
        return "OperationalApp{" +
                "id=" + id +
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
                ", commitDate='" + commitDate + '\'' +
                ", progress='" + progress + '\'' +
                ", poiUserId='" + poiUserId + '\'' +
                '}';
    }
}