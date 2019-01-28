package com.amt.wechat.form.snap;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-28 15:15
 * @version 1.0
 */
public class FrameGoods  implements Serializable {
    private static final long serialVersionUID = 1101058800426526164L;


    /**
     *  操购状态;
     *
     * 1:开抢结束;2:当前抢购;3:即将开始
     */
    private int frameStatus;

    /**
     * 时段
     */
    private String timeFrames;

    /**
     * 时段物品
     */
    private List<SnapGoodsForm> goodsList;

    public FrameGoods() {

    }

    public FrameGoods(int frameStatus, String timeFrames) {
        this.frameStatus = frameStatus;
        this.timeFrames = timeFrames;
        this.goodsList = new ArrayList<>();
    }

    public int getFrameStatus() {
        return frameStatus;
    }

    public void setFrameStatus(int frameStatus) {
        this.frameStatus = frameStatus;
    }

    public String getTimeFrames() {
        return timeFrames;
    }

    public void setTimeFrames(String timeFrames) {
        this.timeFrames = timeFrames;
    }

    public List<SnapGoodsForm> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<SnapGoodsForm> goodsList) {
        this.goodsList = goodsList;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}