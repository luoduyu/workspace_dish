package com.wmt.wechat.model.member;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *  会员卡
 *
 * @author adu Create on 2019-01-10 14:28
 * @version 1.0
 */
public class MemberCardData implements Serializable {
    private static final long serialVersionUID = -9182109597784936405L;

    private int id;
    private String name;

    /**
     * 时长单位:DAY/WEEK/MONTH/YEAR
     */
    private String durationUnit;

    /**
     * 时长
     */
    private int duration;

    /**
     * 卖价,单位:分
     */
    private int price;

    /**
     * 显示顺序
     */
    private int showSeq;

    /**
     * 是否主推;0:否,1:是
     */
    private int mainRecmd;

    /**
     * 新用户立减额度,单位:分
     */
    private int discount =0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getShowSeq() {
        return showSeq;
    }

    public void setShowSeq(int showSeq) {
        this.showSeq = showSeq;
    }
    public int getMainRecmd() {
        return mainRecmd;
    }

    public void setMainRecmd(int mainRecmd) {
        this.mainRecmd = mainRecmd;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "MemberCardData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", durationUnit=" + durationUnit +
                ", duration=" + duration +
                ", price=" + price +
                ", showSeq=" + showSeq +
                '}';
    }
}