package com.wmt.mgr.service.wechat.member.impl;

import com.alibaba.fastjson.JSONObject;
import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.common.Constants;
import com.wmt.mgr.dao.wechat.member.PoiMemberDao;
import com.wmt.mgr.model.member.PoiMemberRDData;
import com.wmt.mgr.service.wechat.member.PoiMemberService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service("poiMemberServiceImpl")
public class PoiMemberServiceImpl implements PoiMemberService {

    @Resource
    private PoiMemberDao poiMemberDao;

    @Override
    public BizPacket getPoiMemberList(String orderId, String userMobile,
                                      String poiName, String startTime,
                                      String endTime, int index, int pageSize) {

        JSONObject jsonObject = new JSONObject();
        Integer total = poiMemberDao.countPoiMemberRD(Constants.delSpace(orderId), Constants.delSpace(userMobile), Constants.delSpace(poiName),startTime, endTime);

        if (total == null){
            total = 0;
        }
        jsonObject.put("total",total);

        List<PoiMemberRDData> poiMemberList = poiMemberDao.getPoiMemberList(Constants.delSpace(orderId), Constants.delSpace(userMobile), Constants.delSpace(poiName), startTime, endTime, index, pageSize);

        if (total <= 0 || poiMemberList == null) {
            jsonObject.put("list", Collections.emptyList());
            return BizPacket.success(jsonObject);
        }

        jsonObject.put("list", poiMemberList);
        return BizPacket.success(jsonObject);
    }

    @Override
    public BizPacket getPoiMemberByOrderId(String orderId) {

        List<PoiMemberRDData> poiMemberRDList = poiMemberDao.getPoiMemberRDDataByOrderId(orderId);

        JSONObject jsonObject = new JSONObject();

        if(poiMemberRDList == null || poiMemberRDList.size() <= 0){
            jsonObject.put("list",Collections.emptyList());
            return BizPacket.success(jsonObject);
        }
        jsonObject.put("list",poiMemberRDList);

        return BizPacket.success(jsonObject);
    }
}
