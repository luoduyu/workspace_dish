package com.amt.wechat.model.poster;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
public class Poster implements Serializable {
    private static final long serialVersionUID = 2568389395708747432L;

    private String id;
    private int platform;
    private int cateId;
    private String title;
    private JSONArray banner;
    private JSONArray rendering;
    private long mPrice;
    private long price;
    private Date cTime;
    private Date uTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public long getmPrice() {
        return mPrice;
    }

    public void setmPrice(long mPrice) {
        this.mPrice = mPrice;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }

    public Date getuTime() {
        return uTime;
    }

    public void setuTime(Date uTime) {
        this.uTime = uTime;
    }

    @Override
    public String toString() {
        return "Poster{" +
                "id='" + id + '\'' +
                ", platform=" + platform +
                ", cateId=" + cateId +
                ", title='" + title + '\'' +
                ", banner='" + banner.toString() + '\'' +
                ", rendering=" + rendering.toString() +
                ", mPrice=" + mPrice +
                ", price=" + price +
                ", cTime=" + cTime +
                ", uTime=" + uTime +
                '}';
    }
}