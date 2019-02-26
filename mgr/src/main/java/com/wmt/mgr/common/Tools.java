package com.wmt.mgr.common;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author lujunp Create on 2019/2/26 16:38
 * @version 1.0
 */
public class Tools {

    /**
     * 起始页
     */
    public static final Integer INDEX = 0;

    /**
     * 每页数据量
     */
    public static final Integer PAGESIZE = 20;

    /**
     * 去掉字符串空格
     * @param param     待查参数
     * @return          目标值
     */
    public static String delSpace(String param){
        if(param != null){
            return param.trim();
        }
        return "";
    }

}
