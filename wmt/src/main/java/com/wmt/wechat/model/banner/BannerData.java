package com.wmt.wechat.model.banner;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-27 16:39
 * @version 1.0
 */
public class BannerData implements Serializable {
    private static final long serialVersionUID = -8215336536182854033L;

    private Integer id;
    /**
     * 名称
     */
    private String name;

    /**
     * 显示顺序
     */
    private Integer showSeq;

    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     *
     */
    private String link;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getShowSeq() {
        return showSeq;
    }

    public void setShowSeq(Integer showSeq) {
        this.showSeq = showSeq;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "BannerData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", showSeq=" + showSeq +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}