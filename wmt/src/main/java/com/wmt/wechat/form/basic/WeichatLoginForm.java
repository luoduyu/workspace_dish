package com.wmt.wechat.form.basic;

import com.wmt.wechat.form.poi.PoiForm;

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

    /**
     * 店铺userId
     */
    private String puid;

    private String accessToken;


    private String nickName;
    private String avatarUrl="";

    private String name;
    private String mobile;

    /**
     * 身份信息;-1:身份未知(默认),0:否,1:是;(每店只能有一名店主)
     */
    private int isMaster;


    /**
     * 当前分享金余额
     */
    private int shareBalance =0;


    /**
     * 店铺信息
     */
    private PoiForm poiBasicData;





    public WeichatLoginForm() {

    }


    public String getPuid() {
        return puid;
    }

    public void setPuid(String puid) {
        this.puid = puid;
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

    public int getShareBalance() {
        return shareBalance;
    }

    public void setShareBalance(int shareBalance) {
        this.shareBalance = shareBalance;
    }

    public PoiForm getPoiBasicData() {
        return poiBasicData;
    }

    public void setPoiBasicData(PoiForm poiBasicData) {
        this.poiBasicData = poiBasicData;
    }

    @Override
    public String toString() {
        return "WeichatLoginForm{" +
                "puid='" + puid + '\'' +
                "accessToken='" + accessToken + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", isMaster=" + isMaster +
                '}';
    }
}