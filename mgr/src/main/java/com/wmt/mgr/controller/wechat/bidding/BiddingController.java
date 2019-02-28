package com.wmt.mgr.controller.wechat.bidding;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.domain.annotation.PostMappingEx;
import com.wmt.mgr.domain.rabc.permission.MgrModules;
import com.wmt.mgr.service.wechat.bidding.BiddingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  竞价帐户
 *
 * @author adu Create on 2019-02-26 14:37
 * @version 1.0
 */
@RestController
public class BiddingController {
    private static Logger logger = LoggerFactory.getLogger(BiddingController.class);

    private @Resource  BiddingService biddingService;


    @PostMappingEx(value = "/mgr/bidding/poi/list",funcName = "店铺竞价帐户浏览",module = MgrModules.YUNYING)
    public BizPacket poiList(String brandName,String masterName,String masterMobile,Integer index,Integer pageSize){
        if(index == null || index <0 || index >= Integer.MAX_VALUE){
            index = 0;
        }
        if(pageSize == null || pageSize <0 || pageSize >= Integer.MAX_VALUE){
            pageSize = 20;
        }
        if(brandName != null){
            brandName=brandName.trim();
        }
        if(masterName != null){
            masterName=masterName.trim();
        }
        if(masterMobile != null){
            masterMobile=masterMobile.trim();
        }

        try {
            return biddingService.poiList(brandName,masterName,masterMobile,index,pageSize);
        } catch (Exception e) {
            logger.error("brandName="+brandName+",maserName="+masterName+",masterMobile="+masterMobile+",index="+index+",pageSize="+pageSize+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }
}
