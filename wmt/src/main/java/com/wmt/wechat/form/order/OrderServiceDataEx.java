package com.wmt.wechat.form.order;

import com.alibaba.fastjson.JSON;
import com.wmt.wechat.model.order.OrderServiceData;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-07 13:42
 * @version 1.0
 */
public class OrderServiceDataEx extends OrderServiceData implements Serializable {

    private static final long serialVersionUID = -1451456486169303490L;
    /**
     * 服务总得分
     */
    private int totalScore;

    public void init(OrderServiceData data) {
        setOrderId(data.getOrderId());
        setServicerId(data.getServicerId());
        setServicerName(data.getServicerName());
        setCreateTime(data.getCreateTime());
        setCommentStatus(data.getCommentStatus());

        if(data.getCommentStatus() != 1){
            return;
        }

        setScoreService(data.getScoreService());
        setScoreProfess(data.getScoreProfess());
        setScoreResponse(data.getScoreResponse());
        setCommentText(data.getCommentText());
        setCommentUserId(data.getCommentUserId());
        setCommentTime(data.getCommentTime());
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}