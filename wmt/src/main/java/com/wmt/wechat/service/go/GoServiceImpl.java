package com.wmt.wechat.service.go;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.commons.util.DateTimeUtil;
import com.wmt.wechat.dao.go.GoDao;
import com.wmt.wechat.dao.poi.PoiDao;
import com.wmt.wechat.form.yunying.ShenQingForm;
import com.wmt.wechat.model.go.GOData;
import com.wmt.wechat.model.poi.PoiData;
import com.wmt.wechat.model.poi.PoiUserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-02 19:35
 * @version 1.0
 */
@Service("goService")
public class GoServiceImpl implements  GoService {

    private static final Logger logger = LoggerFactory.getLogger(GoServiceImpl.class);

    private @Resource GoDao goDao;
    private @Resource PoiDao poiDao;

    @Override
    public BizPacket requestFormSubmit(ShenQingForm form, PoiUserData userData, ShenQingType usefor) {
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        PoiData poiData = poiDao.getPoiData(userData.getPoiId());
        if(poiData == null){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        if(form.getAmount() <= 0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"门店数量必须大于0!");
        }
        GOData data = create(form,userData.getId(),poiData,usefor);
        goDao.addRequestForm(data);
        return BizPacket.success(data.getId());
    }

    private GOData create(ShenQingForm form, String userId ,PoiData poiData,ShenQingType usefor){
        GOData data = new GOData();

        data.setUsefor(usefor.ordinal());
        data.setBrandName(form.getBrandName());
        data.setAmount(form.getAmount());
        data.setDishCateId(form.getDishCateId());
        data.setPlatform(form.getPlatform());
        data.setPoiId(poiData.getId());

        data.setProvince(form.getProvince());
        data.setCity(form.getCity());
        data.setDistricts(form.getDistricts());
        data.setAddress(form.getAddress());

        data.setPoiType(form.getPoiType());
        data.setPoiUserId(userId);

        data.setAuditDate(DateTimeUtil.now());
        data.setAuditStatus(0);
        data.setOpinion("");

        data.setCreateTime(data.getAuditDate());
        data.setUpdTime(data.getUpdTime());
        return data;
    }

    @Override
    public BizPacket requestFormGet(PoiUserData userData,ShenQingType usefor){
        GOData data = goDao.getDataByPOIId(userData.getPoiId(),usefor.ordinal());
        if(data == null){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"还未提交过申请!");
        }

        return BizPacket.success(data);
    }

    @Override
    public BizPacket requestFormReSubmit(PoiUserData userData,int id){
        GOData goData = goDao.getDataById(id);
        if(goData == null){
            return BizPacket.error(HttpStatus.NOT_ACCEPTABLE.value(),"此前并未提交过申请,请重新提交,谢谢!");
        }

        if(goData.getAuditStatus() ==0){
            return BizPacket.success();
            //return BizPacket.error(HttpStatus.CONFLICT.value(),"申请已进入待审核状态，无须重提交!");
        }
        goData.setPoiUserId(userData.getId());
        goDao.updateData(userData.getId(),DateTimeUtil.now(),id);
        return BizPacket.success();
    }
}