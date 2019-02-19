package com.amt.wechat.domain.annotation;

import com.amt.wechat.domain.rabc.permission.FunctionModules;
import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-19 09:38
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping
public @interface RequestMappingEx {

    @AliasFor(annotation = RequestMapping.class)
    String[] value() default {};


    @AliasFor(annotation = RequestMapping.class)
    String[] path() default {};


    /**
     * 功能名称
     * @return
     */
    public String funcName() default "";


    /**
     * 所属模块名称
     * @return
     */
    public FunctionModules module() default FunctionModules.NONE;

}
