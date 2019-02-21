package com.wmt.wechat.model.poster;

import java.io.Serializable;

/**
 * Copyright (c) 2018 by CANSHU
 *  海报销量排行榜
 *
 * @author adu Create on 2018-12-26 14:44
 * @version 1.0
 */
public class TopPosterData implements Serializable {
    private static final long serialVersionUID = 1432797446453353752L;


    /**
     * 期限单位;1:"DAYS"(天),2:"HOURS"(小时)
     */
    //private int timeUnit;

    /**
     * 1:最近一个月/周/天;2:最近两个月/周/天;3:最近三个月/周/天
     */
    //private int expiresIn;

    private int posterId;
    private int sales;


    public int getPosterId() {
        return posterId;
    }

    public void setPosterId(int posterId) {
        this.posterId = posterId;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    @Override
    public String toString() {
        return "TopPosterData{" +
                ", posterId='" + posterId + '\'' +
                ", sales=" + sales +
                '}';
    }
}