package com.amt.wechat.model.poi;

import java.io.Serializable;

/**
 * Copyright (c) 2018 by CANSHU
 *
 *  店铺用户信息之微信部分
 * @author adu Create on 2018-12-18 13:53
 * @version 1.0
 */
public class POIUserDataWX implements Serializable {
    private static final long serialVersionUID = 4147968524582176272L;
    /**
     * 微信信息Id
     */
    private String wxId;

    private String authToken;

    /**
     * 性别 0：未知、1：男、2：女
     */
    private int gender;

    private  String country;
    private String province;
    private String city;

    private String unionid="";
    private String openid="";
    private String avatarUrl="";
    private String nickName="";

    private String createDate;

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
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

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "POIUserDataWX{" +
                "wxId='" + wxId + '\'' +
                ", authToken='" + authToken + '\'' +
                ", gender=" + gender +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", unionid='" + unionid + '\'' +
                ", openid='" + openid + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", nickName='" + nickName + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}