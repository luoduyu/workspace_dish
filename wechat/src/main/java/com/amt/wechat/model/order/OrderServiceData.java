package com.amt.wechat.model.order;

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

    private String orderId;
    private int servicerId;
    private String servicerName;
    private int scoreService;
    private int scoreProfess;
    private int scoreResponse;
    private String commentText;

    /**
     * 服务员总评价
     */
    private int totalScore;

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

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
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