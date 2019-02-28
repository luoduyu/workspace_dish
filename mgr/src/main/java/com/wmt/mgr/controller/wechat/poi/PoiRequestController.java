package com.wmt.mgr.controller.wechat.poi;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.common.Constants;
import com.wmt.mgr.domain.annotation.PostMappingEx;
import com.wmt.mgr.domain.rabc.permission.MgrModules;
import com.wmt.mgr.service.wechat.poi.PoiRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Copyright (c) 2019 by CANSHU
 * <p>
 * 店铺申请控制层
 *
 * @author lujunp Create on 2019/2/27 19:59
 * @version 1.0
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class PoiRequestController {

    private static final Logger logger = LoggerFactory.getLogger(PoiRequestController.class);

    @Resource
    private PoiRequestService poiRequestService;

    @PostMappingEx(value = "/mgr/poi/request/list", funcName = "店铺申请记录列表", module = MgrModules.YUNYING)
    public BizPacket queryPoiRequestList(Integer id, String poiUserName, String poiUserMobile,
                                         String brandName, String province,
                                         String city, String districts, String startTime,
                                         String endTime, Integer index, Integer pageSize) {

        if (startTime != null && !startTime.trim().isEmpty()
                && endTime != null && !endTime.trim().isEmpty()) {
            if (startTime.compareTo(endTime) > 0) {
                return BizPacket.error(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value(), "请求参数开始时间大于结束时间");
            }
        }

        if (index == null || index < 0 || index >= Integer.MAX_VALUE) {
            index = Constants.INDEX;
        }
        if (pageSize == null || pageSize < 0 || pageSize >= Integer.MAX_VALUE) {
            pageSize = Constants.PAGESIZE;
        }

        return poiRequestService.queryPoiRequestList(id, poiUserName, poiUserMobile, brandName, province, city, districts, startTime, endTime, index, pageSize);

    }

    @PostMappingEx(value = "/mgr/poi/request/detail", funcName = "店铺申请详情", module = MgrModules.YUNYING)
    public BizPacket queryPoiRequestDetail(Integer id){

        if (id == null){
            return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(), "服务单编号不能为空");
        }

        return poiRequestService.queryPoiRequestDetail(id);

    }

}
