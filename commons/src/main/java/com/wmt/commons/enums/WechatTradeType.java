package com.wmt.commons.enums;

/**
 * Copyright (c) 2019 by CANSHU
 *  微信交易类型
 *
 * @author adu Create on 2019-01-21 19:00
 * @version 1.0
 */
public enum WechatTradeType {

    /**
     * JSAPI支付(或小程序支付)
     */
    JSAPI("JSAPI","JSAPI支付（或小程序支付）"),


    /**
     * Native支付
     */
    NATIVE("NATIVE","Native支付"),

    /**
     * app支付
     */
    APP("APP","app支付"),

    /**
     * H5支付
     */
    H5("MWEB","H5支付");


    /**
     * key
     */
    private String key;


    /**
     * 描述
     */
    private String drcp;

    private WechatTradeType(String key,String drcp){
        this.key = key;
        this.drcp = drcp;
    }

    public String key() {
        return key;
    }

    public String getDrcp() {
        return drcp;
    }
}