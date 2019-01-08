package com.amt.wechat.form.basic;

import com.alibaba.fastjson.JSON;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-04 16:53
 * @version 1.0
 */
public class AllBasicSettingForm extends BasicSettingForm {
    private static final long serialVersionUID = -642755104570230593L;


    /**
     * 店主手机号
     */
    private String memberMobile;

    /**
     * 国家
     */
    private String poiCountry="中国";

    public String getMemberMobile() {
        return memberMobile;
    }

    public void setMemberMobile(String memberMobile) {
        this.memberMobile = memberMobile;
    }

    public String getPoiCountry() {
        return poiCountry;
    }

    public void setPoiCountry(String poiCountry) {
        this.poiCountry = poiCountry;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}