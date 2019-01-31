package com.amt.wechat.model.member;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *  会员反馈
 *
 * @author adu Create on 2019-01-31 19:21
 * @version 1.0
 */
public class MemberFeedbackData implements Serializable {
    private static final long serialVersionUID = 3056823494456949283L;
    private Integer id;

    private String userId;

    /**
     * 店铺id
     */
    private String poiId;

    /**
     * 服务质量
     */
    private String svcQty;

    /**
     * 建议文本
     */
    private String suggestText;

    /**
     * 创建时间戳
     */
    private String createTime;

    /**
     * 状态;0:未处理;1:已受理;2:处理完毕
     */
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getSvcQty() {
        return svcQty;
    }

    public void setSvcQty(String svcQty) {
        this.svcQty = svcQty;
    }

    public String getSuggestText() {
        return suggestText;
    }

    public void setSuggestText(String suggestText) {
        this.suggestText = suggestText;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}