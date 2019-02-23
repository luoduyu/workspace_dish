package com.wmt.mgr.form.wechat.member;

import com.alibaba.fastjson.JSON;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-23 17:23
 * @version 1.0
 */
public class CardForm {

    private Integer id;

    @NotEmpty(message = "会员卡名称必填!")
    private String name;

    /**
     * 时长单位:DAY/WEEK/MONTH/YEAR
     */
    @NotEmpty(message = "'会员时长单位'必填!")
    private String durationUnit;

    /**
     * 时长
     */
    @Size(min = 1,max = Integer.MAX_VALUE)
    private int duration;

    /**
     * 卖价,单位:分
     */
    @Size(min = 1,max = Integer.MAX_VALUE)
    private int price;

    /**
     * 是否主推;0:否,1:是
     */
    @Size(min = 0,max = 1)
    private int mainRecmd;

    /**
     * 新用户立减额度,单位:分
     */
    @Size(min = 0,max = Integer.MAX_VALUE)
    private int newDiscount =0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public int getMainRecmd() {
        return mainRecmd;
    }

    public void setMainRecmd(int mainRecmd) {
        this.mainRecmd = mainRecmd;
    }

    public int getNewDiscount() {
        return newDiscount;
    }

    public void setNewDiscount(int newDiscount) {
        this.newDiscount = newDiscount;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}