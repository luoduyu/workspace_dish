package com.wmt.mgr.service.wechat.poi.impl;

import com.alibaba.fastjson.JSONObject;
import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.common.Constants;
import com.wmt.mgr.dao.wechat.poi.PoiRequestDao;
import com.wmt.mgr.dao.wechat.poi.PoiUserDao;
import com.wmt.mgr.model.poi.PoiRequestData;
import com.wmt.mgr.model.poi.PoiUserData;
import com.wmt.mgr.service.wechat.poi.PoiRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author lujunp Create on 2019/2/27 18:00
 * @version 1.0
 */
@Service("poiRequestServiceImpl")
public class PoiRequestServiceImpl implements PoiRequestService {

    @Resource
    private PoiUserDao poiUserDao;
    @Resource
    private PoiRequestDao poiRequestDao;

    @Override
    public BizPacket queryPoiRequestList(Integer id, String poiUserName,
                                         String poiUserMobile, String brandName,
                                         String province, String city,
                                         String districts, String startTime,
                                         String endTime, int index, int pageSize) {

        String poiUserId = "";
        if (poiUserMobile != null || poiUserName != null) {

            PoiUserData poiUserData = poiUserDao.selectPoiUser(poiUserName, poiUserMobile);

            if (poiUserData == null) {
                return BizPacket.error(HttpStatus.GONE.value(), "条件用户信息不存在");
            }
            poiUserId = poiUserData.getId();
        }

        List<PoiRequestData> poiRequestDataList = poiRequestDao.selectPoiRequestList(id, poiUserId, Constants.delSpace(brandName), Constants.delSpace(province),
                Constants.delSpace(city), Constants.delSpace(districts), startTime, endTime, index, pageSize);

        JSONObject jsonObject = new JSONObject();

        if (poiRequestDataList == null || poiRequestDataList.size() <= 0) {
            jsonObject.put("list", Collections.emptyList());
            return BizPacket.success(jsonObject);
        }
        jsonObject.put("list", poiRequestDataList);
        return BizPacket.success(jsonObject);
    }

    @Override
    public BizPacket queryPoiRequestDetail(int id) {

        JSONObject jsonObject = new JSONObject();

        PoiRequestData poiRequestData = poiRequestDao.selectPoiRequestDetail(id);

        if (poiRequestData == null) {
            return BizPacket.error(HttpStatus.GONE.value(), "服务单编号详情不存在id=" + id);
        }

        PoiUserData poiUserData = poiUserDao.selectPoiUserById(poiRequestData.getPoiUserId());
        if (poiUserData != null) {
            jsonObject.put("poiUserMobile", poiUserData.getMobile());
            jsonObject.put("poiUserName", poiUserData.getName());
        } else {
            jsonObject.put("poiUserMobile", "");
            jsonObject.put("poiUserName", "");
        }

        jsonObject.put("id", poiRequestData.getId());
        jsonObject.put("brandName", poiRequestData.getBrandName());
        jsonObject.put("province", poiRequestData.getProvince());
        jsonObject.put("city", poiRequestData.getCity());
        jsonObject.put("districts", poiRequestData.getDistricts());
        jsonObject.put("address", poiRequestData.getAddress());
        jsonObject.put("platform", poiRequestData.getPlatform());
        jsonObject.put("poiType", poiRequestData.getPoiType());
        jsonObject.put("dishCateId", poiRequestData.getDishCateId());
        jsonObject.put("amount", poiRequestData.getAmount());
        jsonObject.put("createTime", poiRequestData.getCreateTime());

        return BizPacket.success(jsonObject);
    }

}
