package com.amt.wechat.controller.poi;

import com.amt.wechat.controller.base.BaseController;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.service.balance.BalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  店铺类controller
 *
 * @author adu Create on 2019-01-03 20:24
 * @version 1.0
 */
@RestController
public class PoiController extends BaseController {
    private @Resource  BalanceService balanceService;

    /**
     * 余额充值
     * @param amount
     * @return
     */
    @PostMapping(value = "/balance/recharge/in")
    public BizPacket balanceRecharge(@RequestParam("amount") Integer amount){
        if(StringUtils.isEmpty(amount) || amount < 10000 || amount>= Integer.MAX_VALUE){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"充值金额不符合规定!");
        }

        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }
        return balanceService.recharge(userData,amount);

    }


    /**
     *  余额充值记录
     * @param index
     * @param pageSize
     * @return
     */
    @PostMapping(value = "/balance/recharge/in/list")
    public BizPacket getMyRechargeData(Integer index,Integer pageSize){
        if(index == null){
            index = 0;
        }
        if(pageSize== null){
            pageSize = 20;
        }

        if(index<0 || index >= Integer.MAX_VALUE){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"页索引参数非法!");
        }
        if(pageSize<0 || pageSize >= 100){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"页大小参数非法!");
        }


        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }
        return balanceService.getMyRechargeData(userData,index,pageSize);
    }


    /**
     * 余额充值消费记录
     *
     * @param index
     * @param pageSize
     * @return
     */
    @PostMapping(value = "/balance/recharge/out/list")
    public BizPacket getMyConsumeData(Integer index,Integer pageSize){
        if(index == null){
            index = 0;
        }
        if(pageSize== null){
            pageSize = 20;
        }

        if(index<0 || index >= Integer.MAX_VALUE){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"页索引参数非法!");
        }
        if(pageSize<0 || pageSize >= 100){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"页大小参数非法!");
        }


        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        return balanceService.getMyConsumeData(userData,index,pageSize);
    }
}