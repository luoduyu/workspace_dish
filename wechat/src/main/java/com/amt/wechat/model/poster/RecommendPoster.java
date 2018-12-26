package com.amt.wechat.model.poster;

import java.io.Serializable;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-26 11:42
 * @version 1.0
 */
public class RecommendPoster implements Serializable {
    private static final long serialVersionUID = -7071347273214159780L;

    private String posterId;

    /**
     * 显示顺序
     */
    private int showSeq;

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    public int getShowSeq() {
        return showSeq;
    }

    public void setShowSeq(int showSeq) {
        this.showSeq = showSeq;
    }

    @Override
    public String toString() {
        return "RecommendPoster{" +
                "posterId='" + posterId + '\'' +
                ", showSeq=" + showSeq +
                '}';
    }
}