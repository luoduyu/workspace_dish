package com.amt.wechat.domain.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-29 10:30
 * @version 1.0
 */
@Configuration
public class MyWebMvcConfigurer  implements WebMvcConfigurer {

    private @Resource WechatHandlerInterceptor wechatHandlerInterceptor;
    private @Resource MgrHandlerInterceptor mgrHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(wechatHandlerInterceptor).addPathPatterns("/go/**","/setting/**","/order/**","/bidding/recharge/**","/member/**","/balance/recharge/**","/invite/**");

        //registry.addInterceptor(mgrHandlerInterceptor).addPathPatterns("/dish/**");

    }
}