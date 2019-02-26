package com.wmt.mgr.domain.rabc.permission;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-20 14:55
 * @version 1.0
 */
public enum MgrModules {
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



    YUNYING(45,"运营管理"),


    /**
     * 竞价帐户
     */
    BIDDING(50,"竞价帐户"),


    SHARE(55,"分享金帐户"),


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

    private MgrModules(int id, String name){
        this.id = id;
        this.name= name;
    }
}