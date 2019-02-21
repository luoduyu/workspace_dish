package com.wmt.wechat.model.poster;

import com.alibaba.fastjson.JSON;
import com.wmt.wechat.model.common.GoodsData;

import java.io.Serializable;

/**
 * Copyright (c) 2018 by CANSHU
 *
 *  海报
 *
 * @author adu Create on 2018-12-25 13:28
 * @version 1.0
 */
public class PosterData extends GoodsData implements Serializable {
    private static final long serialVersionUID = 2568389395708747432L;

    private int platform;
    private int cateId;

    private String rendering;
    private String updTime;
    /**
     * 海报是否可用;0:不可用;1:可用;
     */
    private int  isEnabled;

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

    public String getRendering() {
        return rendering;
    }

    public void setRendering(String rendering) {
        this.rendering = rendering;
    }
    public String getUpdTime() {
        return updTime;
    }

    public void setUpdTime(String updTime) {
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
        return JSON.toJSONString(this);
    }
}