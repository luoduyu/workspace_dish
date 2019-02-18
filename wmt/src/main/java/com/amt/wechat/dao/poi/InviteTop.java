package com.amt.wechat.dao.poi;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-14 16:39
 * @version 1.0
 */
public class InviteTop {

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
     * 收入
     */
    private long totalShare;

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

    public long getTotalShare() {
        return totalShare;
    }

    public void setTotalShare(long totalShare) {
        this.totalShare = totalShare;
    }
}
