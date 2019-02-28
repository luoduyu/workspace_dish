package com.wmt.mgr.model.member;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 * 会员充值记录
 *
 * @author lujunp Create on 2019/2/28 17:33
 * @version 1.0
 */
public class PoizMemberList implements Serializable {

    private static final long serialVersionUID = -228821961363432889L;
    /**
     * 购买人
     */
    private String name = "";

    /**
     * 会员卡时长单位
     */
    private String durationUnit;

    /**
     * 支付完成时间，格式为yyyy-MM-dd HH:mm:ss
     */
    private String timeEnd;

    /**
     * 购买日期
     */
    private String buyTime;
    /**
     * 店铺老板手机
     */
    private String poiUserMobile = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public String getPoiUserMobile() {
        return poiUserMobile;
    }

    public void setPoiUserMobile(String poiUserMobile) {
        this.poiUserMobile = poiUserMobile;
    }
}
