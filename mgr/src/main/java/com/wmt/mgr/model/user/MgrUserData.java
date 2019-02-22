package com.wmt.mgr.model.user;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-22 11:57
 * @version 1.0
 */
public class MgrUserData implements Serializable {

    private static final long serialVersionUID = 8650122481013364254L;
    /**
     * id
     */
    private Integer id;

    private String accessToken;

    /**
     * 帐户是否未过期;0:否;1:是
     */
    private Integer isAccountNonExpired;

    /**
     * 帐户是否未被冻结
     */
    private Integer isAccountNonLocked;

    /**
     * 帐户密码是否未过期(一般有的密码要求性高的系统会使用到，比较每隔一段时间就要求用户重置密码)
     */
    private Integer isCredentialsNonExpired;

    /**
     * 帐号是否可用;0:不可用;1:可用
     */
    private Integer isEnabled;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 性别 0：未知、1：男、2：女
     */
    private Integer gender;

    /**
     * 真实姓名
     */
    private String name;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 创建时间戳
     */
    private String createTime;

    /**
     * 最后修改时间戳
     */
    private String updTime;

    /**
     * 密码
     */
    private String password;



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getIsAccountNonExpired() {
        return isAccountNonExpired;
    }

    public void setIsAccountNonExpired(Integer isAccountNonExpired) {
        this.isAccountNonExpired = isAccountNonExpired;
    }

    public Integer getIsAccountNonLocked() {
        return isAccountNonLocked;
    }

    public void setIsAccountNonLocked(Integer isAccountNonLocked) {
        this.isAccountNonLocked = isAccountNonLocked;
    }

    public Integer getIsCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public void setIsCredentialsNonExpired(Integer isCredentialsNonExpired) {
        this.isCredentialsNonExpired = isCredentialsNonExpired;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdTime() {
        return updTime;
    }

    public void setUpdTime(String updTime) {
        this.updTime = updTime;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
