package com.wmt.commons.enums;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  支付状态
 *
 * @author adu Create on 2019-01-08 10:50
 * @version 1.0
 */
public enum PayStatus {

    /**
     * 不代表任何值,占位符
     */
    NONE(0),

    /**
     * 1:未付款
     */
    NOT_PAID(1),

    /**
     * 已付款
      */
    PAIED(2);


    private int value;

    private PayStatus(int value){
        this.value = value;
    }

    public int value() {
        return value;
    }
}
