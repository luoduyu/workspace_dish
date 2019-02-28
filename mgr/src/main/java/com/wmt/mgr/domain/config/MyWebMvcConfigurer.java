package com.wmt.mgr.domain.config;

import com.wmt.mgr.common.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-29 10:30
 * @version 1.0
 */
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    private @Resource MgrInterceptor mgrInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(mgrInterceptor);
        interceptorRegistration.excludePathPatterns(Constants.excludePatterns);
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsRegistration corsRegistration= registry.addMapping("/**");
        corsRegistration.allowedMethods("GET","POST","PUT","DELETE","OPTION");
        //corsRegistration.allowedMethods("*");

        corsRegistration.allowCredentials(true);
        corsRegistration.maxAge(3600);

        String[] origins = {"https://www.beta.com","http://www.beta.com","http://www.waimaitong.xin","https://www.waimaitong.xin","http://www.jiedankuai.com","https://www.jiedankuai.com","http://192.168.0.*","https://192.168.0.*"};
        corsRegistration.allowedOrigins("*");
        //corsRegistration.allowedOrigins("*");
    }
}