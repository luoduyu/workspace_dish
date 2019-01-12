/*
watermark参数说明：

        参数	类型	说明
        watermark	OBJECT	数据水印
        appid	String	敏感数据归属appid，开发者可校验此参数与自身appid是否一致
        timestamp	DateInt	敏感数据获取的时间戳, 开发者可以用于数据时效性校验
*/

package com.amt.wechat.service.poi.impl;

import com.alibaba.fastjson.JSONObject;
import com.amt.wechat.dao.poi.PoiDao;
import com.amt.wechat.dao.poi.PoiUserDao;
import com.amt.wechat.domain.id.Generator;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.DateTimeUtil;
import com.amt.wechat.domain.util.WechatUtil;
import com.amt.wechat.form.basic.WeichatLoginForm;
import com.amt.wechat.model.poi.*;
import com.amt.wechat.service.poi.EmplIdentity;
import com.amt.wechat.service.poi.IPoiUserService;
import com.amt.wechat.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-17 10:40
 * @version 1.0
 */
@Service("poiUserService")
public class PoiUserServiceImpl implements IPoiUserService {

    private static Logger logger = LoggerFactory.getLogger(PoiUserServiceImpl.class);
    private @Resource PoiUserDao poiUserDao;
    private @Resource PoiDao poiDao;
    private @Resource RedisService redisService;

    @Override
    public BizPacket weichatLogin(String code, String encryptedData, String iv) {
        JSONObject sessionKeyAndOpenid = WechatUtil.getSessionKeyOrOpenId(code);
        if(sessionKeyAndOpenid == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"登录凭证校验失败!");
        }

        logger.info("用户[sessionKey_openid={},code={}]正在从微信登录!",sessionKeyAndOpenid.toString(),code);

        String sessionKey = sessionKeyAndOpenid.getString("session_key");
        String openid = sessionKeyAndOpenid.getString("openid");

        try {
            PoiUserData userData =  poiUserDao.getPOIUserDataByOpenid(openid);
            if(userData == null){
                JSONObject userJson =  WechatUtil.getUserInfo(encryptedData,sessionKey,iv);
                logger.info("创建微信用户!userJson={}",userJson);

                userData = createUser(sessionKeyAndOpenid,userJson);
                poiUserDao.addPOIUser(userData);

            }else{

                JSONObject userJson =  WechatUtil.getUserInfo(encryptedData,sessionKey,iv);
                logger.info("更新微信用户!userJson={}",userJson);
                updateUser(userData,sessionKeyAndOpenid,userJson);
                poiUserDao.updatePOIUser(userData);
            }

            redisService.addPoiUser(userData);

            //long now = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            WeichatLoginForm form = buildResponse(userData);
            return BizPacket.success(form);
        } catch (IOException e) {
            logger.error("e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }



    private PoiUserData createUser(JSONObject sessionKeyAndOpenid,JSONObject userJson){
        PoiUserData data = new PoiUserData();
        data.setPoiId("");
        data.setId(Generator.uuid());
        data.setCreateTime(DateTimeUtil.now());
        data.setUpdTime(data.getCreateTime());
        data.setOpenid(sessionKeyAndOpenid.getString("openid"));
        data.setIsAccountNonExpired(1);
        data.setIsAccountNonLocked(1);
        data.setIsCredentialsNonExpired(1);
        data.setIsEnabled(1);
        data.setIsMaster(1);
        data.setName("");

        data.setPassword("");
        updateUser(data,sessionKeyAndOpenid,userJson);
        return data;
    }


    private void updateUser(PoiUserData data,JSONObject sessionKeyAndOpenid,JSONObject userJson){
        data.setAccessToken(Generator.uuid());

        // unionid 不一定存在!
        String unionid = sessionKeyAndOpenid.getString("unionid");
        if(unionid != null && unionid.trim().length() != 0) {
            data.setUnionid(unionid);
        }else{
            data.setUnionid("");
        }

        if(userJson == null){
            return;
        }
        Integer gender = userJson.getInteger("gender");
        data.setGender(gender == null ?0:gender);

        String province = userJson.getString("province");
        data.setProvince(province == null?"":province);

        String city = userJson.getString("city");
        data.setCity(city == null?"":city);

        String nickName = userJson.getString("nickName");
        data.setNickName(nickName == null?"":nickName);

        String avatarUrl = userJson.getString("avatarUrl");
        data.setAvatarUrl(avatarUrl == null ?"":avatarUrl);
    }

    @Override
    public BizPacket auth4Mobile(String name, String mobile, PoiUserData userData, EmplIdentity identity){
        PoiCandidate candidate = null;
        try {
            candidate = poiUserDao.getPoiCandidate(mobile);
            if(candidate == null || StringUtils.isEmpty(candidate.getPoiId())){
                return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(),"抱歉,还没有店铺老板邀请你,暂时无法挂靠店铺!");
            }

            PoiData poiData = poiDao.getPoiData(candidate.getPoiId());
            if(poiData == null){
                return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(),"抱歉,邀请你的店铺已经不存在!");
            }

            // 个人资料之缓存的更新
            userData.setPoiId(candidate.getPoiId());
            userData.setName(name);
            userData.setMobile(mobile);
            redisService.addPoiUser(userData);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()+",mobile="+mobile);
        }
        try {
            // 个人资料之持久化
            poiUserDao.updatePOIUserNameAndMobile(userData.getPoiId(),name,mobile,userData.getId());


            // 删除邀请
            poiUserDao.removeInvoteById(candidate.getId());
            return BizPacket.success();
        } catch (Exception e) {
            logger.error("name="+name+",newMobile="+mobile+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Override
    public BizPacket mobileReplace(PoiUserData currUserData,String newMobile){
        try {
            currUserData.setMobile(newMobile);
            redisService.addPoiUser(currUserData);
        } catch (IOException e) {
            logger.error("newMobile="+newMobile+",e="+e.getMessage(),e);
        }
        try {
            poiUserDao.updatePOIUserMobile(newMobile,currUserData.getId());
            return BizPacket.success();
        } catch (Exception e) {
            logger.error("newMobile="+newMobile+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Override
    public BizPacket updatePoiUserName(PoiUserData userData,String name){
        try {
            userData.setName(name);
            poiUserDao.updatePOIUserName(name,userData.getId());
            redisService.addPoiUser(userData);
            return BizPacket.success();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Override
    public BizPacket testLogin() throws IOException {
        PoiUserData userData = poiUserDao.getPOIUserDataById("c226527e25c5425ea95d9340486cf2d9");
        userData.setAccessToken(Generator.uuid());
        poiUserDao.updatePOIUser(userData);

        redisService.addPoiUser(userData);

        //long now = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        WeichatLoginForm form = buildResponse(userData);
        return BizPacket.success(form);
    }

    @Override
    public WeichatLoginForm buildResponse(PoiUserData userData){
        WeichatLoginForm form = new WeichatLoginForm();
        form.setAccessToken(userData.getAccessToken());
        form.setNickName(userData.getNickName());
        form.setAvatarUrl(userData.getAvatarUrl());
        form.setName(userData.getName());
        form.setMobile(userData.getMobile());
        form.setIsMaster(userData.getIsMaster());

        form.setIsAuthDone(0);
        form.setBalancePwdSet(false);

        if(StringUtils.isEmpty(userData.getPoiId())) {
            return form;
        }

        PoiData poiData = poiDao.getPoiData(userData.getPoiId());
        if(poiData != null){
            PoiBasicData basicData = createFrom(poiData);
            form.setPoiBasicData(basicData);

            if(StringUtils.isEmpty(userData.getMobile())
                    || StringUtils.isEmpty(poiData.getEleShopId())
                    || StringUtils.isEmpty(poiData.getMtAppAuthToken())){
                form.setIsAuthDone(0);
            }else{
                form.setIsAuthDone(1);
            }
            form.setBalancePwdSet(!StringUtils.isEmpty(poiData.getBalancePwd()));
        }
        return form;
    }

    private List<PoiBasicUserData> transfer(List<PoiUserData> employeeList){
        List<PoiBasicUserData> basicUserList = new ArrayList<>(employeeList.size());
        for(PoiUserData u:employeeList){
            PoiBasicUserData data = createFrom(u);
            basicUserList.add(data);
        }
        return basicUserList;
    }

    private PoiBasicData createFrom(PoiData o){
        PoiBasicData basicData = new PoiBasicData();

        basicData.setId(o.getId());
        basicData.setName(o.getName());
        basicData.setBrandName(o.getBrandName());
        basicData.setCateId(o.getCateId());

        basicData.setCountry(o.getCountry());
        basicData.setProvince(o.getProvince());
        basicData.setCity(o.getCity());
        basicData.setDistricts(o.getDistricts());
        basicData.setStreet(o.getStreet());
        basicData.setAddress(o.getAddress());

        return basicData;
    }

    private PoiBasicUserData createFrom(PoiUserData o){
        PoiBasicUserData data = new PoiBasicUserData();
        data.setAvatarUrl(o.getAvatarUrl());
        data.setCreateTime(o.getCreateTime());
        data.setId(o.getId());
        data.setName(o.getName());
        data.setNickName(o.getNickName());
        return data;
    }

    @Override
    public BizPacket employeeList(PoiUserData userData){
        List<PoiUserData> employeeList = poiUserDao.getPoiEmployeeList(userData.getPoiId());
        if(employeeList == null || employeeList.isEmpty()){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"暂时没有成员!");
        }
        List<PoiBasicUserData> basicUserList = transfer(employeeList);
        return BizPacket.success(basicUserList);
    }
}