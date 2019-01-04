package com.amt.wechat.model.poi;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * 装修材料
 *
 * @author adu Create on 2019-01-03 20:12
 * @version 1.0
 */
public class MaterialData implements Serializable {
    private static final long serialVersionUID = -7433379779844871154L;

    private int id;

    /**
     * 显示顺序
     */
    private int showSeq;

	private String name;
	private String coverImg;
	private int price;
	private int memberPrice;
    private String createTime;

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

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
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

    public int getShowSeq() {
        return showSeq;
    }

    public void setShowSeq(int showSeq) {
        this.showSeq = showSeq;
    }

    @Override
    public String toString() {
        return "MaterialData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coverImg='" + coverImg + '\'' +
                ", price=" + price +
                ", memberPrice=" + memberPrice +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}