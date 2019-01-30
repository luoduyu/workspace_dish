package com.amt.wechat.form.snap;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-29 13:17
 * @version 1.0
 */
public class SnapCateForm implements Serializable {
    private static final long serialVersionUID = 4227165754239971878L;


    private Integer id;

    /**
     * 标题
     */
    private String name;


    /**
     * 封面图片地址
     */
    private String coverImg;

    /**
     * 描述
     */
    private String drcp;


    /**
     * 标签
     */
    private String tags;

    /**
     * 显示顺序
     */
    private Integer showSeq;



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


    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getDrcp() {
        return drcp;
    }

    public void setDrcp(String drcp) {
        this.drcp = drcp;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getShowSeq() {
        return showSeq;
    }

    public void setShowSeq(Integer showSeq) {
        this.showSeq = showSeq;
    }

    @Override
    public String toString() {
        return "SnapCateForm{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coverImg='" + coverImg + '\'' +
                ", drcp='" + drcp + '\'' +
                ", tags='" + tags + '\'' +
                ", showSeq=" + showSeq +
                '}';
    }
}
