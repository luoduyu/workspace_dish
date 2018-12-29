package com.amt.wechat.common;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-18 19:48
 * @version 1.0
 */
public class RedisConstants {



    /**
     * 有效期:15天(60分 * 24H * 15天)w
     */
    public static final long TOKEN_TIMEOUT = 60 * 24 * 15;

    public static final String PREFIX_POI_USER = "wmt_poi:user:";
    public static final String PRE_ACCESS_TOKEN = "wmt_wechat:access_token:";
}
