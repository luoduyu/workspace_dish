package com.amt.wechat.model.poi;

import com.alibaba.fastjson.JSON;
import com.amt.wechat.form.poi.PoiForm;
import org.springframework.util.StringUtils;

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
     * 国家
     */
    private String country="中国";

    /**
     * 省份
     */
    private String province;

    /**
     * 市
     */
    private String city;


    /**
     * 区
     */
    private String districts;

    /**
     * 街道
     */
    private String street;


    /**
     * 详细地址
     */
    private String address;


    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 经营品类
     */
    private int cateId;


    /**
     * 图片地址
     */
    private String logoImg;


    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 是否有设置余额支付密码;false:没有设置,true:有设置
     */
    private boolean isBalancePwdSet = false;


    /**
     * 是否余额免密支付,0:否,1:是
     */
    private int balancePwdFree;

    /**
     * 当前帐户余额,单位:分
     */
    private int curBalance;



    /**
     * 当前红包帐户余额,单位:分
     */
    private int curRedBalance;

    /**
     * 当前竞价充值余额,单位:分
     */
    private int curBiddingBalance;

    /**
     * 当前分享金余额,单位:分
     */
    private int currShareBalance;

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

    /**
     * 最后更新时间
     */
    private String updTime;


    /**
     * 余额支付密码
     */
    private String balancePwd;



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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getDistricts() {
        return districts;
    }

    public void setDistricts(String districts) {
        this.districts = districts;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(String logoImg) {
        this.logoImg = logoImg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isBalancePwdSet() {
        return isBalancePwdSet;
    }

    public void setBalancePwdSet(boolean balancePwdSet) {
        isBalancePwdSet = balancePwdSet;
    }


    public int getCurBalance() {
        return curBalance;
    }

    public void setCurBalance(int curBalance) {
        this.curBalance = curBalance;
    }


    public int getCurRedBalance() {
        return curRedBalance;
    }

    public void setCurRedBalance(int curRedBalance) {
        this.curRedBalance = curRedBalance;
    }

    public int getCurBiddingBalance() {
        return curBiddingBalance;
    }

    public void setCurBiddingBalance(int curBiddingBalance) {
        this.curBiddingBalance = curBiddingBalance;
    }

    public int getCurrShareBalance() {
        return currShareBalance;
    }

    public void setCurrShareBalance(int currShareBalance) {
        this.currShareBalance = currShareBalance;
    }

    public int getBalancePwdFree() {
        return balancePwdFree;
    }

    public void setBalancePwdFree(int balancePwdFree) {
        this.balancePwdFree = balancePwdFree;
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


    public String getUpdTime() {
        return updTime;
    }

    public void setUpdTime(String updTime) {
        this.updTime = updTime;
    }

    public String getBalancePwd() {
        return balancePwd;
    }

    public void setBalancePwd(String balancePwd) {
        this.balancePwd = balancePwd;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static PoiForm createFrom(PoiData o, PoiAccountData accountData,String masterMobile){
        PoiForm basicData = new PoiForm();

        basicData.setId(o.getId());
        basicData.setName(o.getName());
        basicData.setBrandName(o.getBrandName());
        basicData.setCateId(o.getCateId());

        basicData.setCountry(o.getCountry());
        basicData.setProvince(o.getProvince());
        basicData.setCity(o.getCity());
        basicData.setDistricts(o.getDistricts());
        basicData.setStreet(o.getStreet());
        basicData.setAddress(o.getAddress());
        basicData.setBalancePwdSet(!StringUtils.isEmpty(o.getBalancePwd()));
        basicData.setLogoImg(o.getLogoImg());

        basicData.setCurRedBalance(accountData.getCurRedBalance());
        basicData.setCurBalance(accountData.getCurBalance());
        basicData.setCurBiddingBalance(accountData.getCurBiddingBalance());
        basicData.setCurrShareBalance(accountData.getCurrShareBalance());

        basicData.setBalancePwdFree(o.getBalancePwdFree());
        basicData.setMasterMobile(masterMobile);

        if(!StringUtils.isEmpty(o.getEleShopId())){
            basicData.setEleAuth(true);
        }else{
            basicData.setEleAuth(false);
        }
        if(!StringUtils.isEmpty(o.getMtAppAuthToken())){
            basicData.setMtAuth(true);
        }else{
            basicData.setMtAuth(false);
        }

        return basicData;
    }
}