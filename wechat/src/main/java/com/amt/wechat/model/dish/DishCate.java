package com.amt.wechat.model.dish;

import java.io.Serializable;

/**
 * Copyright (c) 2018 by CANSHU
 *
 *  菜品分类
 *
 * @author adu Create on 2018-12-25 13:26
 * @version 1.0
 */
public class DishCate implements Serializable {
    private static final long serialVersionUID = -3664574065814055847L;

    private int cateId;
    private String name;
    private int nameSeq;
    private String subName;
    private int subSeq;

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public int getNameSeq() {
        return nameSeq;
    }

    public void setNameSeq(int nameSeq) {
        this.nameSeq = nameSeq;
    }

    public int getSubSeq() {
        return subSeq;
    }

    public void setSubSeq(int subSeq) {
        this.subSeq = subSeq;
    }

    @Override
    public String toString() {
        return "DishCate{" +
                "cateId=" + cateId +
                ", name='" + name + '\'' +
                ", nameSeq=" + nameSeq +
                ", subName='" + subName + '\'' +
                ", subSeq=" + subSeq +
                '}';
    }
}