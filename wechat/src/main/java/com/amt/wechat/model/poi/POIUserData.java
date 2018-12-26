package com.amt.wechat.model.poi;

import java.io.Serializable;

/**
 * 店铺用户信息
 */
public class POIUserData implements Serializable {
    private static final long serialVersionUID = -1608316652654376276L;


    private String id;

    /**
     * 登录用户名(帐户，非昵称)
     */
    private String username;

    /**
     * 密码信息
     */
    private String password;


    private String mobile="";

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
     * 是否店主;0:否;1:是
     */
    private int isMaster;

    private String authToken;

    private String createDate;

    /**
     * 微信信息Id
     */
    private String wxId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    @Override
    public String toString() {
        return "poiUserData{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mobile='" + mobile + '\'' +
                ", isAccountNonExpired=" + isAccountNonExpired +
                ", isAccountNonLocked=" + isAccountNonLocked +
                ", isCredentialsNonExpired=" + isCredentialsNonExpired +
                ", isEnabled=" + isEnabled +
                ", isMaster=" + isMaster +
                ", authToken='" + authToken + '\'' +
                ", createDate='" + createDate + '\'' +
                ", wxId='" + wxId + '\'' +
                '}';
    }
}