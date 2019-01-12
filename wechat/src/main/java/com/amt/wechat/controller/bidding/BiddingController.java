package com.amt.wechat.controller.bidding;

import com.amt.wechat.controller.base.BaseController;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.service.bidding.BiddingService;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  竞价类controller
 *
 * @author adu Create on 2019-01-08 17:11
 * @version 1.0
 */
@RestController
public class BiddingController extends BaseController {
    private @Resource BiddingService biddingService;
//
//    @GetMapping(value = "/bidding/stage/list")
//    public BizPacket stageDataListGet(){
//        List<BiddingStageData> list = biddingService.getStageDataList();
//        return BizPacket.success(list);
//    }


    /**
     *  竞价充值记录
     * @param index
     * @param pageSize
     * @return
     */
    @PostMapping(value = "/bidding/recharge/in/list")
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
        return biddingService.getMyRechargeData(userData,index,pageSize);
    }


    /**
     * 竞价充值消费记录
     *
     * @param index
     * @param pageSize
     * @return
     */
    @PostMapping(value = "/bidding/recharge/out/list")
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

        return biddingService.getMyConsumeData(userData,index,pageSize);
    }


    /**
     * 竞价充值
     * @param amount
     * @return
     */
    @PostMapping(value = "/bidding/recharge/in")
    public BizPacket recharge(@RequestParam("amount") Long amount){
        if(StringUtils.isEmpty(amount) || amount < 10000 || amount>= Integer.MAX_VALUE){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"充值金额不符合规定!");
        }

        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }
        return biddingService.recharge(userData,amount.intValue());
    }
}