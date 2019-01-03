package com.amt.wechat.service.go;

import com.amt.wechat.dao.go.GoDao;
import com.amt.wechat.dao.poi.PoiUserDao;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.DateTimeUtil;
import com.amt.wechat.form.ShenQingForm;
import com.amt.wechat.model.go.GOData;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-02 19:35
 * @version 1.0
 */
@Service("goService")
public class GoServiceImpl implements  GoService {

    private static final Logger logger = LoggerFactory.getLogger(GoServiceImpl.class);

    private @Resource  GoDao goDao;
    private @Resource PoiUserDao userDAO;
    private @Resource RedisService redisService;

    @Override
    public BizPacket requestFormSubmit(ShenQingForm form, PoiUserData userData) {
        GOData data = create(form,userData);
        goDao.addRequestForm(data);

        // 若之前手机号是空的，则填充之
        if(StringUtils.isEmpty(userData.getMobile())){
            userData.setMobile(form.getContactMobile());
            userData.setName(form.getContactName());
            userDAO.updatePOIUserNameAndMobile(userData.getName(),userData.getMobile(),userData.getId());
            try {
                redisService.addPoiUser(userData);
            } catch (IOException e) {
               logger.error(e.getMessage(),e);
            }
        }
        return BizPacket.success(data.getId());
    }

    private GOData create(ShenQingForm form, PoiUserData userData){
        GOData data = new GOData();

        data.setUsefor(form.getUsefor());
        data.setBrandName(form.getBrandName());
        data.setAmount(form.getAmount());
        data.setDishCateId(form.getDishCateId());
        data.setPlatform(form.getPlatform());
        data.setPoiId(userData.getPoiId());

        data.setProvince(form.getProvince());
        data.setCity(form.getCity());
        data.setDistricts(form.getDistricts());
        data.setAddress(form.getAddress());

        data.setPoiType(form.getPoiType());
        data.setPoiUserId(userData.getId());
        data.setContactMobile(form.getContactMobile());
        data.setContactName(form.getContactName());

        data.setAuditDate(DateTimeUtil.now());
        data.setAuditStatus(0);
        data.setOpinion("");

        data.setcTime(data.getAuditDate());
        data.setuTime(data.getcTime());
        return data;
    }

    @Override
    public BizPacket requestFormGet(PoiUserData userData,int usefor){
        GOData data = goDao.getDataByPOIId(userData.getPoiId(),usefor);
        if(data == null){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"还未提交过申请!");
        }
        return BizPacket.success(data);
    }

    @Override
    public BizPacket requestFormReSubmit(int id){
        GOData goData = goDao.getDataById(id);
        if(goData == null){
            return BizPacket.error(HttpStatus.NOT_ACCEPTABLE.value(),"此前并未提交过申请,请重新提交,谢谢!");
        }

        if(goData.getAuditStatus() ==0){
            return BizPacket.error(HttpStatus.CONFLICT.value(),"申请已进入待审核状态，无须重提交!");
        }
        goDao.updateData(DateTimeUtil.now(),id);
        return BizPacket.success();
    }
}