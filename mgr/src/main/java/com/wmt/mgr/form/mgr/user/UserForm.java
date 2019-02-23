package com.wmt.mgr.form.mgr.user;

import com.alibaba.fastjson.JSON;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-23 14:34
 * @version 1.0
 */
public class UserForm implements Serializable {
    private static final long serialVersionUID = -5179968600758684451L;

    /**
     * id
     */
    private Integer id;

    /**
     * 帐号是否可用;0:不可用;1:可用
     */
    @NotNull
    private Integer isEnabled;

    /**
     * 真实姓名
     */
    @NotEmpty(message = "姓名不为能空!")
    private String name;


    /**
     * 手机号
     */
    @NotEmpty(message = "手机号为必填项!")
    @Size(min = 11,max = 11)
    private String mobile;

    /**
     *
     */
    @NotEmpty(message = "密码不为能空!")
    @Size(min = 3,max = 20)
    private String password;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}