package com.amt.wechat.form.poi;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  '我'(店铺)的会员数据
 *
 * @author adu Create on 2019-01-10 15:54
 * @version 1.0
 */
public class MyMemberDataForm implements Serializable {
    private static final long serialVersionUID = -2032936678129251307L;

    /**
     * 会员ID
     */
    private int memberId;

    /**
     * 节省花费,单位:分
     */
    private int costSave= 0;

    /**
     * 会员卡时长单位:DAY/WEEK/MONTH/YEAR
     */
    private String durationUnit="MONTH";

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
     * 当前是否自动续费;0:否,1:是
     */
    private int autoFeeRenew=0;


    /**
     * 自动续费金额,单位:分
     */
    private int autoFee;

    /**
     * 默认不续费
     */
    public MyMemberDataForm() {
        this(0,"MONTH","","",0,0);
    }

    public MyMemberDataForm(int costSave, String durationUnit, String buyTime, String expiredAt, int autoFeeRenew, int autoFee) {
        this.costSave = costSave;
        this.duration =0;
        this.durationUnit = durationUnit;
        this.buyTime = buyTime;
        this.expiredAt = expiredAt;
        this.autoFeeRenew = autoFeeRenew;
        this.autoFee = autoFee;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getCostSave() {
        return costSave;
    }

    public void setCostSave(int costSave) {
        this.costSave = costSave;
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
        return "MyMemberDataForm{" +
                "costSave=" + costSave +
                ", durationUnit=" + durationUnit +
                ", buyTime='" + buyTime + '\'' +
                ", expiredAt='" + expiredAt + '\'' +
                ", autoFeeRenew=" + autoFeeRenew +
                ", autoFee=" + autoFee +
                '}';
    }
}