package com.wmt.mgr.model.order;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 * 订单列表
 *
 * @author lujunp Create on 2019/2/26 13:41
 * @version 1.0
 */
@Alias("orderData")
public class OrderData implements Serializable {

    private static final long serialVersionUID = 5178103309313091776L;
    /**
     * 店铺Id
     */
    private String poiId;

    /**
     * 店铺名称
     */
    private String poiName;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 物品类型;1:海报;2:装修服务;3:抢购物品
     */
    private int goodsType;

    /**
     * 商品总价,单位:分
     */
    private int total;


    /**
     * (通过余额或者微信)实付金额;单位:分;
     */
    private int payment;


    /**
     * 支付状态;1:待付款;2:已付款;
     */
    private int payStatus;


    /**
     * 订单创建时间
     */
    private String createTime;


    /**
     * 服务状态;0:未处理;1:处理中;2:已完成
     */
    private int serviceStatus;

    /**
     * 支付途径,1:银联,2:支付宝,3:微信,4:余额支付
     */
    private Integer payWay=3;



    /**
     * 微信支付订单号
     */
    private String transactionId="";


    /**
     * 支付完成时间
     */
    private String timeEnd="";

    /**
     * 摘要
     */
    private String summary="";


    /**
     * 购买人
     */
    private String submitUserId="";

    /**
     * 购买人手机
     */
    private String submitUserMobile="";

    /**
     * 购买人姓名
     */
    private String submitUserName="";
    /**
     * 付款人
     */
    private String payUserId="";


    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(int serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSubmitUserId() {
        return submitUserId;
    }

    public void setSubmitUserId(String submitUserId) {
        this.submitUserId = submitUserId;
    }

    public String getSubmitUserMobile() {
        return submitUserMobile;
    }

    public void setSubmitUserMobile(String submitUserMobile) {
        this.submitUserMobile = submitUserMobile;
    }

    public String getSubmitUserName() {
        return submitUserName;
    }

    public void setSubmitUserName(String submitUserName) {
        this.submitUserName = submitUserName;
    }

    public String getPayUserId() {
        return payUserId;
    }

    public void setPayUserId(String payUserId) {
        this.payUserId = payUserId;
    }
}
