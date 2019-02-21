package com.wmt.wechat.model.snap;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-31 10:43
 * @version 1.0
 */
public class CurrentTimeFrameData implements Serializable {
    private static final long serialVersionUID = -842895024942298919L;

    /**
     * 起始时间
     */
    private String timeStart;

    /**
     * 结束时间
     */
    private String timeEnd;

    /**
     * 当前时段库存量
     */
    private Integer stockNum;

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }
}