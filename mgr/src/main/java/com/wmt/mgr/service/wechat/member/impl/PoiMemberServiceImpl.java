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

        //订单基本信息
        PoiMemberRDData poiMemberRDData = poiMemberDao.selectPoiMemberRD(orderId);
        if (poiMemberRDData == null) {
            return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(), "未找到订单号对应订单基本信息，orderId=" + orderId);
        }

        List<PoiMemberRDData> poiMemberRDList = poiMemberDao.getPoiMemberRDDataByOrderId(orderId);

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("orderId",poiMemberRDData.getOrderId());
        jsonObject.put("userMobile",poiMemberRDData.getUserMobile());
        jsonObject.put("timeEnd",poiMemberRDData.getTimeEnd());
        jsonObject.put("buyTime",poiMemberRDData.getBuyTime());
        jsonObject.put("payWay",poiMemberRDData.getPayWay());
        jsonObject.put("payStatus",poiMemberRDData.getPayStatus());
        jsonObject.put("poiName",poiMemberRDData.getPoiName());
        jsonObject.put("total",poiMemberRDData.getTotal());
        jsonObject.put("discount",poiMemberRDData.getDiscount());

        if(poiMemberRDList == null || poiMemberRDList.size() <= 0){
            jsonObject.put("list",Collections.emptyList());
            return BizPacket.success(jsonObject);
        }
        jsonObject.put("list",poiMemberRDList);

        return BizPacket.success(jsonObject);
    }
}
