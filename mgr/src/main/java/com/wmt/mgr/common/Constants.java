package com.wmt.mgr.common;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-22 15:03
 * @version 1.0
 */
public class Constants {


    /**
     * 已登录的
     */
    public static final String  MGR_LOGGED_USER  = "mgr_logged_user";
    public static final String REQ_PARAM_ACCESSTOKEN = "accessToken";


    public static final String[] excludePatterns ={"/*.html","/index.html","/error","/mgr/login/**","/mgr/sms/**","/mgr/token/**","/mgr/img/**"};

    /**
     * 起始页
     */
    public static final Integer INDEX = 0;

    /**
     * 每页数据量
     */
    public static final Integer PAGESIZE = 10;

    /**
     * 删除参数多余字符串
     * @param param     待格式检查参数
     * @return          目标格式参数
     */
    public static String delSpace(String param){
        if(param != null){
            return param.trim();
        }
        return "";
    }

    public static final String  MGR_IMG_CODE  = "mgr_img_code";
}
