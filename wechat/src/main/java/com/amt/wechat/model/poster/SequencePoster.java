package com.amt.wechat.model.poster;

import com.alibaba.fastjson.JSON;
import com.amt.wechat.model.poster.Poster;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * 带排序的海报
 *
 * @author adu Create on 2018-12-26 11:46
 * @version 1.0
 */
public class SequencePoster extends Poster {
    private static final long serialVersionUID = -5005340082887287160L;

    /**
     * 显示顺序
     */
    private int showSeq = 0;

    public int getShowSeq() {
        return showSeq;
    }

    public void setShowSeq(int showSeq) {
        this.showSeq = showSeq;
    }

    @Override
    public String toString() {
       return JSON.toJSONString(this);
    }
}