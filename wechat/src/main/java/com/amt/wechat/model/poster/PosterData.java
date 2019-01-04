package com.amt.wechat.model.poster;

import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;
import java.util.Date;

/**
 * Copyright (c) 2018 by CANSHU
 *
 *  海报
 *
 * @author adu Create on 2018-12-25 13:28
 * @version 1.0
 */
public class PosterData implements Serializable {
    private static final long serialVersionUID = 2568389395708747432L;

    private int id;
    private int platform;
    private int cateId;
    private String title;
    private JSONArray banner;
    private JSONArray rendering;
    private int memberPrice;
    private int price;
    private Date createTime;
    private Date updTime;

    /**
     * 海报是否可用;0:不可用;1:可用;
     */
    private int  isEnabled;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JSONArray getBanner() {
        return banner;
    }

    public void setBanner(JSONArray banner) {
        this.banner = banner;
    }

    public JSONArray getRendering() {
        return rendering;
    }

    public void setRendering(JSONArray rendering) {
        this.rendering = rendering;
    }

    public int getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(int memberPrice) {
        this.memberPrice = memberPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdTime() {
        return updTime;
    }

    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    public int getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }
    @Override
    public String toString() {
        return "PosterData{" +
                "id='" + id + '\'' +
                ", platform=" + platform +
                ", cateId=" + cateId +
                ", title='" + title + '\'' +
                ", banner='" + banner.toString() + '\'' +
                ", rendering=" + rendering.toString() +
                ", memberPrice=" + memberPrice +
                ", price=" + price +
                ", createTime=" + createTime +
                ", updTime=" + updTime +
                ", isEnabled=" + isEnabled+
                '}';
    }
}