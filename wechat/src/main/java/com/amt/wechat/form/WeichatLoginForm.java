package com.amt.wechat.form;

import java.io.Serializable;

public class  WeichatLoginForm implements Serializable {
    private static final long serialVersionUID = 305892774632439166L;
    private String accessToken;
    private String nickName;
    private String avatarUrl="";

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


    @Override
    public String toString() {
        return "WeichatLoginForm{" +
                "accessToken='" + accessToken + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}