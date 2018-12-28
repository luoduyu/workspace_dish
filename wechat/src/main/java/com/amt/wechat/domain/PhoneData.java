package com.amt.wechat.domain;

import java.io.Serializable;

/**
 * Copyright (c) 2018 by CANSHU
 *
 *  电话号码
 * @author adu Create on 2018-12-27 14:36
 * @version 1.0
 */
public class PhoneData implements Serializable {
    private static final long serialVersionUID = 7882043107491655464L;

    /**
     * 临时存储在session中的电话号码(不带区号)
     */
    public  transient static final String SESSION_PHONE = "POI_USER_PHONE";

    /**
     * 临时存储在session中的电话号码的区号
     */
    public transient static final String SESSION_PHONE_CC = "POI_USER_PHONE_COUNTRYCODE";


    private String purePhoneNumber;
    private String countryCode;

    public PhoneData(String purePhoneNumber, String countryCode) {
        this.purePhoneNumber = purePhoneNumber;
        this.countryCode = countryCode;
    }

    public String getPurePhoneNumber() {
        return purePhoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public String toString() {
        return "PhoneData{" +
                "purePhoneNumber='" + purePhoneNumber + '\'' +
                ", countryCode='" + countryCode + '\'' +
                '}';
    }
}