package com.wmt.wechat.model.poi;

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
     * 邀请者头像地址
     */
    private String userAvatarUrl;

    /**
     * 邀请者昵称
     */
    private String userNickName;

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
     * 被邀请者头像地址
     */
    private String avatarUrl;

    /**
     * 被邀请者昵称
     */
    private String nickName;

    /**
     * 奖励金额,单位:分
     */
    private Integer share;

    /**
     * 被邀请者会员卡时长单位
     */
    private String durationUnit;

    /**
     * 被邀请者会员时长
     */
    private Integer duration;

    /**
     * 日期
     */
    private String createDate;

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}