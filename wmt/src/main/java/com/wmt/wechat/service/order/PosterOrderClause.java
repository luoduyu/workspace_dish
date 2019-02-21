package com.wmt.wechat.service.order;

/**
 * Copyright (c) 2018 by CANSHU
 *
 *  海报排序子句定义
 *
 * @author adu Create on 2018-12-26 17:40
 * @version 1.0
 */
public enum PosterOrderClause {

    /**
     * 0:全部作品
     */
    ALL("createTime ASC"),

    /**
     * 1:最新作品
     */
    LATEST("updTime DESC"),

    /**
     * 2:价格最低
     */
    LOWEST("price ASC"),

    /**
     * 3:人气作品
     */
    SALES("");

    private static PosterOrderClause[] vs = PosterOrderClause.values();
    private String orderClause;
    PosterOrderClause(String orderClause){
        this.orderClause = orderClause;
    }

    public String getClause() {
        return orderClause;
    }

    public static PosterOrderClause valueOf(int index){
        for(PosterOrderClause o:vs){
            if(o.ordinal() == index){
                return o;
            }
        }
        return null;
    }
}