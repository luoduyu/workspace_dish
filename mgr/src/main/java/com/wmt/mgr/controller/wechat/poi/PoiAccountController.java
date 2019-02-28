package com.wmt.mgr.controller.wechat.poi;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.common.Constants;
import com.wmt.mgr.domain.annotation.PostMappingEx;
import com.wmt.mgr.domain.rabc.permission.MgrModules;
import com.wmt.mgr.service.wechat.poi.PoiAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Copyright (c) 2019 by CANSHU
 * 商户账户
 *
 * @author lujunp Create on 2019/2/28 13:42
 * @version 1.0
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class PoiAccountController {

    private static final Logger logger = LoggerFactory.getLogger(PoiAccountController.class);

    @Resource
    private PoiAccountService poiAccountService;

    @PostMappingEx(value = "/mgr/poi/account/list", funcName = "商户账单列表", module = MgrModules.YUNYING)
    public BizPacket queryPoiAccountList(String branchName, String poiUserName,
                                         String poiUserMobile, Integer index, Integer pageSize){

        if(branchName != null && branchName.length() != 0){
            branchName = branchName.trim();
        }
        if(poiUserName != null && branchName.length() != 0){
            poiUserName = poiUserName.trim();
        }
        if(poiUserMobile != null && poiUserMobile.length() != 0){
            poiUserMobile = poiUserMobile.trim();
        }
        if(index == null || index <= 0 || index >= Integer.MAX_VALUE ){
            index = Constants.INDEX;
        }
        if(pageSize == null || pageSize <= 0 || pageSize >= Integer.MAX_VALUE){
            pageSize = Constants.PAGESIZE;
        }

        return poiAccountService.queryPoiAccountList(branchName,poiUserName,poiUserMobile,index,pageSize);
    }

}
