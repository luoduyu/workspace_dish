package com.amt.wechat.common;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-18 19:48
 * @version 1.0
 */
public class RedisConstants {

    public static final String PRE_ACCESS_TOKEN = "wechat:access_token:";

    /**
     * 60分 * 24H * 15天
     */
    public static final long AccessTokenValidM = 60 * 24 * 15;
}
