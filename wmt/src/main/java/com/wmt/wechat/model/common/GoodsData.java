package com.wmt.wechat.model.common;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  物品
 * @author adu Create on 2019-01-07 18:59
 * @version 1.0
 */
public class GoodsData implements Serializable {
    private static final long serialVersionUID = -5373304438237986146L;


    private int id;
    private String name;

    /**
     * 原价
     */
    private int price;

    /**
     * 会员价
     */
    private int memberPrice;


    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 封面图片
     */
    private String coverImg;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(int memberPrice) {
        this.memberPrice = memberPrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }
}