package com.amt.wechat.model.poi;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-12 19:30
 * @version 1.0
 */
public class UserIncomeData implements Serializable {
    private static final long serialVersionUID = 5265331269651345029L;
    /**
     * 邀请者Id
     */
    private String userId;

    /**
     * 店铺Id
     */
    private String poiId;

    /**
     * 被邀请者Id
     */
    private String inviteeId;

    /**
     * 奖励金额,单位:分
     */
    private Integer share;

    /**
     * 日期
     */
    private String createDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public String getInviteeId() {
        return inviteeId;
    }

    public void setInviteeId(String inviteeId) {
        this.inviteeId = inviteeId;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}