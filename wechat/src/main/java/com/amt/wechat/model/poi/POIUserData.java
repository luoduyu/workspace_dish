package com.amt.wechat.model.poi;

import java.io.Serializable;

/**
 * 店铺用户信息
 */
public class POIUserData implements Serializable {
    private static final long serialVersionUID = -1608316652654376276L;
    private String id;

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
     *
     */
    private String roles;

    /**
     * 是否店主;0:否;1:是
     */
    private int isMaster;

    /**
     * 密码信息
     */
    private String password;
    private String mobile="";

    /**
     * 性别 0：未知、1：男、2：女
     */
    private int gender;
    private  String countryCode;
    private String province;
    private String city;
    private String openid="";
    private String unionid="";
    private String name;
    private String nickName="";
    private String avatarUrl="";
    private String cTime;
    private String uTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public int getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(int isMaster) {
        this.isMaster = isMaster;
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

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public String getuTime() {
        return uTime;
    }

    public void setuTime(String uTime) {
        this.uTime = uTime;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "POIUserData{" +
                "id='" + id + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", isAccountNonExpired=" + isAccountNonExpired +
                ", isAccountNonLocked=" + isAccountNonLocked +
                ", isCredentialsNonExpired=" + isCredentialsNonExpired +
                ", isEnabled=" + isEnabled +
                ", isMaster=" + isMaster +
                ", password='" + password + '\'' +
                ", mobile='" + mobile + '\'' +
                ", gender=" + gender +
                ", countryCode='" + countryCode + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", openid='" + openid + '\'' +
                ", unionid='" + unionid + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", cTime='" + cTime + '\'' +
                '}';
    }
}