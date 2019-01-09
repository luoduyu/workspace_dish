package com.amt.wechat.form.basic;

import com.amt.wechat.model.poi.PoiBasicData;

import java.io.Serializable;


/**
 * Copyright (c) 2019 by CANSHU
 *
 *  客户端登录响应信息体
 *
 * @author adu Create on 2019-01-09 16:34
 * @version 1.0
 */
public class  WeichatLoginForm implements Serializable {
    private static final long serialVersionUID = 305892774632439166L;
    private String accessToken;


    private String nickName;
    private String avatarUrl="";

    private String name;
    private String mobile;

    /**
     * 是否店主;0:否;1:是
     */
    private int isMaster;

    /**
     * 店铺信息
     */
    private PoiBasicData poiBasicData;

    /**
     * 是否会员;0:否;1:是
     */
    private int isMember=0;

    /**
     * 是否完成了授权认证
     */
    private int isAuthDone;

    public WeichatLoginForm() {

    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(int isMaster) {
        this.isMaster = isMaster;
    }
    public PoiBasicData getPoiBasicData() {
        return poiBasicData;
    }

    public void setPoiBasicData(PoiBasicData poiBasicData) {
        this.poiBasicData = poiBasicData;
    }

    public int getIsAuthDone() {
        return isAuthDone;
    }

    public void setIsAuthDone(int isAuthDone) {
        this.isAuthDone = isAuthDone;
    }

    public int getIsMember() {
        return isMember;
    }

    public void setIsMember(int isMember) {
        this.isMember = isMember;
    }

    @Override
    public String toString() {
        return "WeichatLoginForm{" +
                "accessToken='" + accessToken + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", isMaster=" + isMaster +
                ", isAuthDone=" + isAuthDone +
                '}';
    }
}