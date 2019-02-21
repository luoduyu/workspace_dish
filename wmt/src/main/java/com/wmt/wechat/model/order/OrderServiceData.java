package com.wmt.wechat.model.order;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * 订单服务评价
 *
 * @author adu Create on 2019-01-05 19:05
 * @version 1.0
 */
@Alias("orderServiceData")
public class OrderServiceData implements Serializable {

    private static final long serialVersionUID = 8000626084263319623L;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 服务人员Id
     */
    private int servicerId;

    /**
     * 服务人员姓名
     */
    private String servicerName;

    /**
     * 创建时间
     */
    private String createTime;


    /**
     * 评价状态;0:未评;1:已评
     */
    private int commentStatus;

    /**
     * 综合服务评分
     */
    private Integer scoreService=0;

    /**
     * 专业能力评分
     */
    private Integer scoreProfess=0;

    /**
     * 响应速度评分
     */
    private Integer scoreResponse=0;

    /**
     * 评价内容
     */
    private String commentText;


    /**
     * 评价者Id
     */
    private String commentUserId;


    /**
     * 评价时间
     */
    private String commentTime;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getServicerId() {
        return servicerId;
    }

    public void setServicerId(int servicerId) {
        this.servicerId = servicerId;
    }

    public String getServicerName() {
        return servicerName;
    }

    public void setServicerName(String servicerName) {
        this.servicerName = servicerName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(int commentStatus) {
        this.commentStatus = commentStatus;
    }

    public int getScoreService() {
        return scoreService;
    }

    public void setScoreService(int scoreService) {
        this.scoreService = scoreService;
    }

    public int getScoreProfess() {
        return scoreProfess;
    }

    public void setScoreProfess(int scoreProfess) {
        this.scoreProfess = scoreProfess;
    }

    public int getScoreResponse() {
        return scoreResponse;
    }

    public void setScoreResponse(int scoreResponse) {
        this.scoreResponse = scoreResponse;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    @Override
    public String toString() {
        return "OrderServiceData{" +
                "orderId='" + orderId + '\'' +
                ", servicerId=" + servicerId +
                ", servicerName='" + servicerName + '\'' +
                ", scoreService=" + scoreService +
                ", scoreProfess=" + scoreProfess +
                ", scoreResponse=" + scoreResponse +
                '}';
    }
}