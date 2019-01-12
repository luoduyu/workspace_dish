package com.amt.wechat.common;

public class Constants {
    public static final String WEICHAT_APP_ID="wx35acf287e0fc1de7";

    public static final String AppSecret ="e9ace1f5c65d12a4e50a6442efb99708";

    public static final  String URL_TEMPLATE = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";


    /**
     * 已登录的
     */
    public static final String  WECHAT_LOGGED_USER  = "wechat_logged_user";
    public static final String REQ_PARAM_ACCESSTOKEN = "accessToken";
}