package com.amt.wechat.model.snap;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  抢购时段
 * @author adu Create on 2019-01-29 13:10
 * @version 1.0
 */
public class SnapTimeframeData implements Serializable {
    private static final long serialVersionUID = -8034228385495084896L;

    /**
     * 时间段Id
     */
    private Integer id;

    /**
     * 所属类别
     */
    private int cateId;


    /**
     * 起始时间
     */
    private String timeStart;

    /**
     * 结束时间
     */
    private String timeEnd;

    /**
     * 显示顺序
     */
    private Integer showSeq;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

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

    public Integer getShowSeq() {
        return showSeq;
    }

    public void setShowSeq(Integer showSeq) {
        this.showSeq = showSeq;
    }

    @Override
    public String toString() {
        return "SnapTimeframeData{" +
                "id=" + id +
                ", cateId=" + cateId +
                ", timeStart='" + timeStart + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                ", showSeq=" + showSeq +
                '}';
    }
}