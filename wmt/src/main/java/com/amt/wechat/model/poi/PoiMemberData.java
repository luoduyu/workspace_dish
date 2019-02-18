package com.amt.wechat.model.poi;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *  店铺会员信息
 *
 * @author adu Create on 2019-01-09 19:29
 * @version 1.0
 */
public class PoiMemberData implements Serializable {
    private static final long serialVersionUID = -1885578530009761558L;


    /**
     * 会员编号
     */
    private int id;

    /**
     * 店铺Id
     */
    private String poiId;


    /**
     * 会员卡时长单位:DAY/WEEK/MONTH/YEAR
     */
    private String durationUnit;


    /**
     * 当前会员时长
     */
    private int duration;

    /**
     * 会员卡购买时间
     */
    private String buyTime;


    /**
     * 会员到期时间
     */
    private String expiredAt;

    /**
     * 当前是否自动续费;0:否(默认),1:是
     */
    private int autoFeeRenew;


    /**
     * 自动续费金额,单位:分
     */
    private int autoFee;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public String getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(String expiredAt) {
        this.expiredAt = expiredAt;
    }

    public int getAutoFeeRenew() {
        return autoFeeRenew;
    }

    public void setAutoFeeRenew(int autoFeeRenew) {
        this.autoFeeRenew = autoFeeRenew;
    }

    public int getAutoFee() {
        return autoFee;
    }

    public void setAutoFee(int autoFee) {
        this.autoFee = autoFee;
    }

    @Override
    public String toString() {
        return "PoiMemberData{" +
                "poiId='" + poiId + '\'' +
                ", durationUnit=" + durationUnit +
                ", buyTime='" + buyTime + '\'' +
                ", expiredAt='" + expiredAt + '\'' +
                ", autoFeeRenew=" + autoFeeRenew +
                ", autoFee=" + autoFee +
                '}';
    }
}