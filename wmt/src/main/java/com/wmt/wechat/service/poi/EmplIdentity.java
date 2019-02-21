package com.wmt.wechat.service.poi;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  用户/成员身份
 *
 * @author adu Create on 2019-01-10 13:49
 * @version 1.0
 */
public enum EmplIdentity {

    /**
     * -1:无
     */
    NONE(-1),

    /**
     * 店员
     */
    EMPLOYEE(1),

    /**
     * 老板
     */
    MASTER(2);

    private int flag;

    private EmplIdentity(int flag){
        this.flag = flag;
    }

    public int value(){
        return this.flag;
    }
}