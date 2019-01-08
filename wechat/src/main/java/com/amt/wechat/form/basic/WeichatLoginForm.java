package com.amt.wechat.form.basic;

import java.io.Serializable;

public class  WeichatLoginForm implements Serializable {
    private static final long serialVersionUID = 305892774632439166L;
    private String accessToken;
    private String nickName;
    private String avatarUrl="";
    private String name;
    private String mobile;

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

    @Override
    public String toString() {
        return "WeichatLoginForm{" +
                "accessToken='" + accessToken + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}