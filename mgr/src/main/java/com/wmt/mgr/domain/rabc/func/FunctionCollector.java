package com.wmt.mgr.domain.rabc.func;

import com.wmt.mgr.domain.annotation.GetMappingEx;
import com.wmt.mgr.domain.annotation.PostMappingEx;
import com.wmt.mgr.domain.annotation.RequestMappingEx;
import com.wmt.mgr.domain.rabc.permission.MgrModules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *  功能收集器
 *
 * @author adu Create on 2019-02-19 15:12
 * @version 1.0
 */
@Component
@Lazy(false)
public class FunctionCollector {
    private static Logger logger = LoggerFactory.getLogger(FunctionCollector.class);


    @PostConstruct
    public void init(){
        List<Class<?>> list = WmtClassUtil.getAllClassByPackageName("com.amt.wechat.controller");
        parseModules(list);
    }

    private void parseModules(List<Class<?>> clsList) throws  IllegalArgumentException{
        if (clsList == null || clsList.isEmpty()) {
            return;
        }

        for (Class<?> cls : clsList) {

            // 获取类中的所有的方法
            Method[] methods = cls.getDeclaredMethods();
            if(methods == null || methods.length <= 0){
                continue;
            }

            for (Method method : methods) {

                PostMappingEx pm = (PostMappingEx) method.getAnnotation(PostMappingEx.class);
                if (pm != null) {
                    ext(cls,method,pm);
                    continue;
                }

                GetMappingEx gm = (GetMappingEx) method.getAnnotation(GetMappingEx.class);
                if (gm != null) {
                    ext(cls,method,gm);
                    continue;
                }

                RequestMappingEx rm = (RequestMappingEx) method.getAnnotation(RequestMappingEx.class);
                if (rm != null) {
                    ext(cls,method,rm);
                    continue;
                }
            }
        }
    }

    private void extract(Class<?> cls,Method method,String funcName, MgrModules module, String url) throws IllegalArgumentException{
        if(funcName == null || funcName.length()==0){
            throw new IllegalArgumentException("required argument:funcName!at:"+cls.getName()+"."+method.getName());
        }
        if(module == null || module == MgrModules.NONE){
            throw new IllegalArgumentException("missed argument:module!at:"+cls.getName()+"."+method.getName());
        }
        if(module == MgrModules.NONE){
            throw new IllegalArgumentException("illegal argument:FunctionModules.NONE!at:"+cls.getName()+"."+method.getName());
        }

        logger.info("功能名称={},所属模块={},url={}",funcName,module,url);
    }

    private void ext(Class<?> cls,Method method,GetMappingEx mapping) throws IllegalArgumentException{
        extract(cls,method,mapping.funcName(),mapping.module(),mapping.value()[0]);
    }
    private void ext(Class<?> cls,Method method,PostMappingEx mapping) throws IllegalArgumentException{
        extract(cls,method,mapping.funcName(),mapping.module(),mapping.value()[0]);
    }
    private void ext(Class<?> cls,Method method,RequestMappingEx mapping) throws IllegalArgumentException{
        extract(cls,method,mapping.funcName(),mapping.module(),mapping.value()[0]);
    }
}