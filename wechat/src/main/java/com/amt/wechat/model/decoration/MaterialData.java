package com.amt.wechat.model.decoration;

import com.alibaba.fastjson.JSON;
import com.amt.wechat.model.common.GoodsData;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * 装修材料
 *
 * @author adu Create on 2019-01-03 20:12
 * @version 1.0
 */
public class MaterialData extends GoodsData implements Serializable {
    private static final long serialVersionUID = -7433379779844871154L;

    /**
     * 显示顺序
     */
    private int showSeq;



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