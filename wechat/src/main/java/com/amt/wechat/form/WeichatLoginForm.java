package com.amt.wechat.form;

import java.io.Serializable;

public class  WeichatLoginForm implements Serializable {
    private static final long serialVersionUID = 305892774632439166L;
    private String authToken;
    private String nickName;
    private String avatarUrl="";
    private long tick = 0;

    public WeichatLoginForm() {

    }

    public WeichatLoginForm(long tick) {
        this.tick = tick;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
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

    public long getTick() {
        return tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

    @Override
    public String toString() {
        return "WeichatLoginForm{" +
                "authToken='" + authToken + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", tick=" + tick +
                '}';
    }
}