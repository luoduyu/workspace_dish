package com.wmt.mgr.model.banner;

import com.alibaba.fastjson.JSON;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-27 17:18
 * @version 1.0
 */
public class BannerData implements Serializable {
    private static final long serialVersionUID = 474313955574042385L;
    private Integer id;

    /**
     * 名称
     */
    @NotNull(message = "名称不能为空!")
    @NotEmpty(message = "名称不能为空!")
    private String name;

    /**
     * 0:未启用;1:已启用
     */
    @Size(min = -1,max = 1)
    @NotNull
    private Integer isEnabled;

    /**
     * 显示顺序
     */
    @NotNull(message = "顺序不能为空!")
    @Size(min = 0,max =Integer.MAX_VALUE)
    private Integer showSeq;

    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     * 显示日期之开始日期:yyyy-MM-dd
     */
    @NotNull(message = "显示日期不能为空!")
    private String expiredStart;

    /**
     * 显示日期之结束日期:yyyy-MM-dd
     */
    @NotNull(message = "显示日期不能为空!")
    private String expiredEnd;


    /**
     *
     */
    private String link;

    /**
     * 创建日期:yyyy-MM-dd
     */
    private String createDate;

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

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
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

    public String getExpiredStart() {
        return expiredStart;
    }

    public void setExpiredStart(String expiredStart) {
        this.expiredStart = expiredStart;
    }

    public String getExpiredEnd() {
        return expiredEnd;
    }

    public void setExpiredEnd(String expiredEnd) {
        this.expiredEnd = expiredEnd;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
