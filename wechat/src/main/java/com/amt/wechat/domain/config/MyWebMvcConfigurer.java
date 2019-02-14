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

    private @Resource  AuthHandlerInterceptor authHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authHandlerInterceptor).addPathPatterns("/go/**","/setting/**","/order/**","/bidding/recharge/**","/member/**","/balance/recharge/**","/invite/**");
    }
}