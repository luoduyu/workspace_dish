package com.amt.wechat.common.constants;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  支付途径
 *
 * @author adu Create on 2019-01-22 14:56
 * @version 1.0
 */
public enum PayWay {

    /**
     * 1:银联
     */
    UNION(1,"银联"),

    /**
     * 2:支付宝
     */
    ALI(2,"支付宝"),

    /**
     * 3:微信
     */
    WECHAT(3,"微信(包括小程序)"),


    /**
     * 4:本地余额帐户支付
     */
    BALANCE(4,"余额支付");


    private int value;

    private String drcp;
    private PayWay(int value,String drcp){
        this.value = value;
        this.drcp = drcp;
    }

    public int value() {
        return value;
    }

    @Override
    public String toString() {
        return drcp;
    }

    public static PayWay valueOf(int payWay){
        for(PayWay p:PayWay.values()){
            if(payWay == p.value()){
                return p;
            }
        }
        return null;
    }
}
