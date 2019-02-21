package com.wmt.wechat.model.poi;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  等待匹配的候选人
 *
 * @author adu Create on 2019-01-10 11:45
 * @version 1.0
 */
public class PoiCandidate implements Serializable {
    private static final long serialVersionUID = 5608346318973471615L;

    private int id;


    private String poiName;

    /**
     * 店铺Id
     */
    private String poiId;

    /**
     * 邀请人
     */
    private String userId;

    /**
     * 候选人手机号
     */
    private String mobile;

    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "PoiCandidate{" +
                "id=" + id +
                ", poiId='" + poiId + '\'' +
                ", userId='" + userId + '\'' +
                ", mobile='" + mobile + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}