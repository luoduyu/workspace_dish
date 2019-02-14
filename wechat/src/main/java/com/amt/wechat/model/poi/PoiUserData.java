package com.amt.wechat.model.poi;

import com.alibaba.fastjson.JSON;

/**
 * Copyright (c) 2019 by CANSHU
 *  店员信息
 * @author adu Create on 2019-01-09 16:25
 * @version 1.0
 */
public class PoiUserData extends PoiBasicUserData {
    private static final long serialVersionUID = -1608316652654376276L;

    /**
     * 所属门店Id
     */
    private String poiId;

    private String accessToken;


    /**
     * 帐户是否未过期;0:否;1:是(默认)
     */
    private int isAccountNonExpired;

    /**
     * 帐户是否未被冻结;0:否;1:是(默认)
     */
    private int isAccountNonLocked;

    /**
     * 帐户密码是否未过期(一般有的密码要求性高的系统会使用到，比较每隔一段时间就要求用户重置密码)0:否;1:是(默认)
     */
    private int isCredentialsNonExpired;

    /**
     * 帐号是否可用;0:不可用;1:可用;
     */
    private int  isEnabled;


    /**
     * 身份信息;-1:身份未知(默认),1:否,2:是;(每店只能有一名店主)
     */
    private int isMaster;


    /**
     * 当前优分享金余额
     */
    private int shareBalance;


    /**
     * 密码信息
     */
    private String password;
    private String mobile="";

    /**
     * 性别 0:未知,1:男,2:女
     */
    private int gender;
    private  String countryCode="";
    private String province;
    private String city;
    private String openid="";
    private String unionid="";
    private String updTime;

    /**
     * 邀请者Id(userId)
     */
    private String inviterId="";



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getIsAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setIsAccountNonExpired(int isAccountNonExpired) {
        this.isAccountNonExpired = isAccountNonExpired;
    }

    public int getIsAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setIsAccountNonLocked(int isAccountNonLocked) {
        this.isAccountNonLocked = isAccountNonLocked;
    }

    public int getIsCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public void setIsCredentialsNonExpired(int isCredentialsNonExpired) {
        this.isCredentialsNonExpired = isCredentialsNonExpired;
    }

    public int getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }


    public int getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(int isMaster) {
        this.isMaster = isMaster;
    }

    public int getShareBalance() {
        return shareBalance;
    }

    public void setShareBalance(int shareBalance) {
        this.shareBalance = shareBalance;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }



    public String getUpdTime() {
        return updTime;
    }

    public void setUpdTime(String updTime) {
        this.updTime = updTime;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public String getInviterId() {
        return inviterId;
    }

    public void setInviterId(String inviterId) {
        this.inviterId = inviterId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


    public static PoiBasicUserData createFrom(PoiUserData o){
        PoiBasicUserData data = new PoiBasicUserData();
        data.setAvatarUrl(o.getAvatarUrl());
        data.setCreateTime(o.getCreateTime());
        data.setId(o.getId());
        data.setName(o.getName());
        data.setNickName(o.getNickName());
        return data;
    }
}