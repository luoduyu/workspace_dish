package com.amt.wechat.form.snap;

import com.alibaba.fastjson.JSON;
import com.amt.wechat.service.snap.SnapStatus;

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
     * 时段开始
     */
    private String timeFrameStart;

    /**
     * 时段结束
     */
    private String timeFrameEnd;


    /**
     * 本时段库存量
     */
    private int timeFrameStockNum;

    /**
     * 已售数量
     */
    private int soldNum = 0;


    /**
     * 时段物品
     */
    private List<SnapGoodsForm> goodsList;

    public FrameGoods() {

    }

    public FrameGoods(SnapStatus frameStatus, String timeFrameStart, String timeFrameEnd, int timeFrameStockNum, Integer soldNum) {
        this.frameStatus = frameStatus.ordinal();
        this.timeFrameStockNum = timeFrameStockNum;

        if(frameStatus == SnapStatus.CURRENT){
            if(soldNum != null) {
                this.soldNum = soldNum;
            }

            // 如果已售完,当前状态置灰
            if(this.soldNum >= this.timeFrameStockNum){
                this.frameStatus = SnapStatus.OVER.ordinal();
            }

        }else if (frameStatus == SnapStatus.OVER){
            this.soldNum = this.timeFrameStockNum;
        }else {
            this.soldNum = 0;
        }

        this.timeFrameStart = timeFrameStart;
        this.timeFrameEnd = timeFrameEnd;

        this.goodsList = new ArrayList<>();
    }


    public int getFrameStatus() {
        return frameStatus;
    }

    public void setFrameStatus(int frameStatus) {
        this.frameStatus = frameStatus;
    }

    public String getTimeFrameStart() {
        return timeFrameStart;
    }

    public void setTimeFrameStart(String timeFrameStart) {
        this.timeFrameStart = timeFrameStart;
    }

    public String getTimeFrameEnd() {
        return timeFrameEnd;
    }

    public void setTimeFrameEnd(String timeFrameEnd) {
        this.timeFrameEnd = timeFrameEnd;
    }

    public int getTimeFrameStockNum() {
        return timeFrameStockNum;
    }

    public void setTimeFrameStockNum(int timeFrameStockNum) {
        this.timeFrameStockNum = timeFrameStockNum;
    }

    public List<SnapGoodsForm> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<SnapGoodsForm> goodsList) {
        this.goodsList = goodsList;
    }


    public int getSoldNum() {
        return soldNum;
    }

    public void setSoldNum(int soldNum) {
        this.soldNum = soldNum;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}