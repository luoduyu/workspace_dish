package com.amt.wechat.service.redis;

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
    public static final long WECHAT_TOKEN_TIMEOUT = 60 * 24 * 15;

    public static final String WECHAT_POI_USER = "wmt_poi:user:";
    public static final String WECHAT_ACCESS_TOKEN = "wmt_wechat:access_token:";

    /**
     * 短信验证码60秒锁
     */
    public static final String WECHAT_SMS = "wmt_wechat:sms:";

    /**
     * 短信验证码每日次数
     */
    public static final String WECHAT_SMS_COUNT = "wmt_wechat:sms_day_count:";

    /**
     * 手机验证码存储
     */
    public static final String WECHAT_SMS_CODE = "wmt_wechat:sms_code:";
}
