package com.wmt.wechat.service.order;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  服务状态;
 *
 * @author adu Create on 2019-01-07 13:37
 * @version 1.0
 */
public enum ServiceStatus {
    /**
     * 0:未处理
      */
    NONE(),
    /**
     * 1:处理中
     */
    DOING,

    /**
     * 2:已完成
     */
    OK;

    public int value(){
        return this.ordinal();
    }
}
