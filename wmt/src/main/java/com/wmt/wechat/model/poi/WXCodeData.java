package com.wmt.wechat.model.poi;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  二维码信息
 *
 * @author adu Create on 2019-02-12 15:40
 * @version 1.0
 */
public class WXCodeData implements Serializable {
    private static final long serialVersionUID = -5108042542963855951L;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 小程序页面URL
     */
    private String shareUrl="";

    /**
     * 小程序二维码图片地址
     */
    private String wxcodeUrl="";


    /**
     * 失效时间
     */
    private String expireDate="";

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getWxcodeUrl() {
        return wxcodeUrl;
    }

    public void setWxcodeUrl(String wxcodeUrl) {
        this.wxcodeUrl = wxcodeUrl;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public String toString() {
        return "WXCodeData{" +
                "userId='" + userId + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                ", wxcodeUrl='" + wxcodeUrl + '\'' +
                '}';
    }
}