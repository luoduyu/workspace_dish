package com.wmt.mgr.controller.wechat.member;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.common.Constants;
import com.wmt.mgr.domain.annotation.PostMappingEx;
import com.wmt.mgr.domain.rabc.permission.MgrModules;
import com.wmt.mgr.service.wechat.member.PoiMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Copyright (c) 2019 by CANSHU
 * 店铺会员订单
 *
 * @author lujunp Create on 2019/2/27 13:39
 * @version 1.0
 */
@RestController
public class PoiMemberRDController {

    private static final Logger logger = LoggerFactory.getLogger(PoiMemberRDController.class);

    @Resource
    private PoiMemberService poiMemberService;

    @PostMappingEx(value = "/mgr/member/order/list", funcName = "会员订单列表", module = MgrModules.MEMBER)
    public BizPacket poiMemberList(String orderId,String userMobile,
                                   String poiName, String startTime,
                                   String endTime, Integer index,
                                   Integer pageSize){

        if (startTime != null && !startTime.trim().isEmpty()
                && endTime !=null && !endTime.trim().isEmpty()) {
            if (startTime.compareTo(endTime) > 0) {
                return BizPacket.error(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value(), "开始时间大于结束时间");
            }
        }
        if (index == null || index < 0 || index >= Integer.MAX_VALUE) {
            index = Constants.INDEX;
        }
        if (pageSize == null || pageSize < 0 || pageSize >= Integer.MAX_VALUE) {
            pageSize = Constants.PAGESIZE;
        }

        return poiMemberService.getPoiMemberList(orderId,userMobile,poiName,startTime,endTime,index,pageSize);
    }


    @PostMappingEx(value = "/mgr/member/order/detail", funcName = "会员订单详情", module =MgrModules.MEMBER )
    public BizPacket getPoiMemberByOrderId(String orderId){
        if(orderId == null || orderId.trim().length() <= 0){
            return BizPacket.error(HttpStatus.GONE.value(),"请求订单号不正确orderId="+orderId);
        }
        return poiMemberService.getPoiMemberByOrderId(orderId.trim());
    }


}
