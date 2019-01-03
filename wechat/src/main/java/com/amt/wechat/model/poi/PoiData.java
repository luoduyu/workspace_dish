package com.amt.wechat.model.poi;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  门店数据
 *
 * @author adu Create on 2019-01-03 17:21
 * @version 1.0
 */
public class PoiData implements Serializable {
    private static final long serialVersionUID = 98370870191036587L;


    /**
     * 门店Id
     */
    private String id;

    /**
     * 门店名称
     */
    private String name;

    /**
     * 门店帐户名
     */
    private String accountName;

    /**
     *
     */
    private String accountPassword;

    /**
     * 美团门店id
     */
    private String mtAppAuthToken;

    /**
     * 饿了么门店Id
     */
    private String eleShopId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public String getMtAppAuthToken() {
        return mtAppAuthToken;
    }

    public void setMtAppAuthToken(String mtAppAuthToken) {
        this.mtAppAuthToken = mtAppAuthToken;
    }

    public String getEleShopId() {
        return eleShopId;
    }

    public void setEleShopId(String eleShopId) {
        this.eleShopId = eleShopId;
    }

    @Override
    public String toString() {
        return "POIData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", accountName='" + accountName + '\'' +
                ", accountPassword='" + accountPassword + '\'' +
                ", mtAppAuthToken='" + mtAppAuthToken + '\'' +
                ", eleShopId='" + eleShopId + '\'' +
                '}';
    }
}