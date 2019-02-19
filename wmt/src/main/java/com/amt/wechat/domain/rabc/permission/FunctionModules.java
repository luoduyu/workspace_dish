package com.amt.wechat.domain.rabc.permission;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  功能模块定义
 *
 * @author adu Create on 2019-02-19 14:41
 * @version 1.0
 */
public enum FunctionModules {
    NONE(0,"未设置"),

    /**
     * 基础设置
     */
    BASIC(10,"基础设置"),

    /**
     * 功能模块及编组/角色管理/权限分配
     */
    USER(20,"人员模块"),


    /**
     * 菜品模块
     */
    DISH(30,"菜品模块"),


    /**
     * 装修管理
     */
    DECORATION(40,"装修模块"),


    /**
     * 运营模块
     */
    YUNYING(50,"运营模块"),


    /**
     * 订单模块
     */
    ORDER(60,"订单模块"),


    /**
     * 会员模块
     */
    MEMBER(70,"会员模块"),


    /**
     * 充值模块
     */
    RECHARGE(80,"充值模块"),


    /**
     * 抢购模块
     */
    SNAP(90,"抢购模块");

    private int id;
    private String name;

    private FunctionModules(int id,String name){
        this.id = id;
        this.name= name;
    }
}
