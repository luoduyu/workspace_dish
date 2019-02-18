package com.amt.wechat.model.go;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  开店申请/运营申请
 *
 * @author adu Create on 2019-01-02 19:40
 * @version 1.0
 */
public class GOData implements Serializable {
    private static final long serialVersionUID = 6347583527222298367L;

    private int id;

    /**
     * 0:开店申请;1:运营申请
     */
    private int usefor;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区
     */
    private String districts;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 已上线餐卖平台;0:空字串;1:美团;2:饿了么;"1,2"表示美团饿了么二者兼备
     */
    private String platform;

    /**
     *
     * 门店类型;1:单店自创品牌;2:连锁加盟;3:连锁直营
     */
    private int poiType;

    /**
     * 经营品类Id
     */
    private int dishCateId;

    /**
     * 门店数量
     */
    private int amount=-1;


    /**
     * 进度;0:待审核;1:审核但未通过;2:审核且通过
     */
    private  int auditStatus;


    /**
     * 审核日期
     */
    private String auditDate;


    /**
     * 门店Id
     */
    private String poiId;


    /**
     * 门店userId
     */
    private String  poiUserId;

    /**
     * 审核意见
     */
    private String opinion;

    /**
     * 提交日期
     */
    private String createTime;


    private String updTime;

    public int getUsefor() {
        return usefor;
    }

    public void setUsefor(int usefor) {
        this.usefor = usefor;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistricts() {
        return districts;
    }

    public void setDistricts(String districts) {
        this.districts = districts;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getPoiType() {
        return poiType;
    }

    public void setPoiType(int poiType) {
        this.poiType = poiType;
    }

    public int getDishCateId() {
        return dishCateId;
    }

    public void setDishCateId(int dishCateId) {
        this.dishCateId = dishCateId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdTime() {
        return updTime;
    }

    public void setUpdTime(String updTime) {
        this.updTime = updTime;
    }

    public String getPoiUserId() {
        return poiUserId;
    }

    public void setPoiUserId(String poiUserId) {
        this.poiUserId = poiUserId;
    }



    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }


    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    @Override
    public String toString() {
        return "GOData{" +
                "id=" + id +
                ", brandName='" + brandName + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", districts='" + districts + '\'' +
                ", address='" + address + '\'' +
                ", platform=" + platform +
                ", poiType=" + poiType +
                ", dishCateId=" + dishCateId +
                ", amount=" + amount +
                ", auditStatus=" + auditStatus +
                ", auditDate='" + auditDate + '\'' +
                ", poiUserId='" + poiUserId + '\'' +
                ", opinion='" + opinion + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updTime='" + updTime + '\'' +
                '}';
    }
}