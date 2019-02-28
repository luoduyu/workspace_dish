package com.wmt.mgr.model.poi;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 * 商户余额账单
 *
 * @author lujunp Create on 2019/2/28 13:33
 * @version 1.0
 */
public class PoiAccountForm implements Serializable {

    private static final long serialVersionUID = -3444750871401617859L;

    /**
     * 门店Id
     */
    private String id;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 老板姓名
     */
    private String poiUserName;

    /**
     * 老板手机
     */
    private String PoiUserMobile;

    /**
     * 当前帐户余额,单位:分
     */
    private int accountTotal;

    /**
     * 当前红包余额,单位:分
     */
    private int presentBalance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getPoiUserName() {
        return poiUserName;
    }

    public void setPoiUserName(String poiUserName) {
        this.poiUserName = poiUserName;
    }

    public String getPoiUserMobile() {
        return PoiUserMobile;
    }

    public void setPoiUserMobile(String poiUserMobile) {
        PoiUserMobile = poiUserMobile;
    }

    public int getAccountTotal() {
        return accountTotal;
    }

    public void setAccountTotal(int accountTotal) {
        this.accountTotal = accountTotal;
    }

    public int getPresentBalance() {
        return presentBalance;
    }

    public void setPresentBalance(int presentBalance) {
        this.presentBalance = presentBalance;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
