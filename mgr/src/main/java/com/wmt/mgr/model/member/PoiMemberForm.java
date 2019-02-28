package com.wmt.mgr.model.member;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 * 会员管理
 *
 * @author lujunp Create on 2019/2/28 16:02
 * @version 1.0
 */
public class PoiMemberForm implements Serializable {

    private static final long serialVersionUID = -1877720205528617030L;

    /**
     * 店铺Id
     */
    private String id;

    /**
     * 品牌名称
     */
    private String branchName = "";

    /**
     * 购买人
     */
    private String name = "";

    /**
     * 会员卡时长单位
     */
    private String durationUnit;

    /**
     * 支付完成时间，格式为yyyy-MM-dd HH:mm:ss
     */
    private String timeEnd;

    /**
     * 购买日期
     */
    private String buyTime;

    /**
     * 店铺老板手机
     */
    private String poiUserMobile = "";

    /**
     * 失效时间
     */
    private String expiredAt = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(String expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String getPoiUserMobile() {
        return poiUserMobile;
    }

    public void setPoiUserMobile(String poiUserMobile) {
        this.poiUserMobile = poiUserMobile;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
