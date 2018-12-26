/*
watermark参数说明：

        参数	类型	说明
        watermark	OBJECT	数据水印
        appid	String	敏感数据归属appid，开发者可校验此参数与自身appid是否一致
        timestamp	DateInt	敏感数据获取的时间戳, 开发者可以用于数据时效性校验
*/

package com.amt.wechat.service.poi.impl;

import com.alibaba.fastjson.JSONObject;
import com.amt.wechat.dao.RedisDao;
import com.amt.wechat.dao.poi.POIUserDAO;
import com.amt.wechat.domain.id.Generator;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.DateTimeUtil;
import com.amt.wechat.domain.util.WeichatUtil;
import com.amt.wechat.form.WeichatLoginForm;
import com.amt.wechat.model.poi.POIUserDataWX;
import com.amt.wechat.service.poi.IPOIUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-17 10:40
 * @version 1.0
 */
@Service("poiUserService")
public class POIUserServiceImpl implements IPOIUserService {

    private static Logger logger = LoggerFactory.getLogger(POIUserServiceImpl.class);
    private @Resource POIUserDAO POIUserDAO;
    private @Resource RedisDao redisDao;

    @Override
    public BizPacket weichatLogin(String code, String encryptedData, String iv) {

        JSONObject sessionKeyAndOpenid = WeichatUtil.getSessionKeyOrOpenId(code);
        if(sessionKeyAndOpenid == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"登录凭证校验失败!");
        }

        logger.info("用户[wxInfo={},code={}]正在从微信登录!",sessionKeyAndOpenid.toString(),code);

        String sessionKey = sessionKeyAndOpenid.getString("session_key");
        String openid = sessionKeyAndOpenid.getString("openid");

        POIUserDataWX userDataWX =  POIUserDAO.getUserDataWXByOpenid(openid);
        if(userDataWX == null){
            JSONObject userJson =  WeichatUtil.getUserInfo(encryptedData,sessionKey,iv);
            logger.info("创建微信用户!userJson={}",userJson);

            userDataWX = createWXUser(sessionKeyAndOpenid,userJson);
            POIUserDAO.addShopUserWX(userDataWX);

        }else{

            JSONObject userJson =  WeichatUtil.getUserInfo(encryptedData,sessionKey,iv);
            logger.info("更新微信用户!userJson={}",userJson);
            updateWXUser(userDataWX,sessionKeyAndOpenid,userJson);
            POIUserDAO.updateShopUserWX(userDataWX);
        }


        redisDao.addAccessToken(userDataWX.getAuthToken(),sessionKey,openid);

        long now = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        WeichatLoginForm form = new WeichatLoginForm(now);
        form.setAuthToken(userDataWX.getAuthToken());
        form.setNickName(userDataWX.getNickName());
        form.setAvatarUrl(userDataWX.getAvatarUrl());

        return BizPacket.success(form);
    }


    private POIUserDataWX createWXUser(JSONObject sessionKeyAndOpenid,JSONObject userJson){
        POIUserDataWX data = new POIUserDataWX();
        data.setWxId(Generator.generate());
        data.setCreateDate(DateTimeUtil.now());
        data.setOpenid(sessionKeyAndOpenid.getString("openid"));

        updateWXUser(data,sessionKeyAndOpenid,userJson);
        return data;
    }


    private void updateWXUser(POIUserDataWX data,JSONObject sessionKeyAndOpenid,JSONObject userJson){
        data.setAuthToken(Generator.generate());

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

        String country = userJson.getString("country");
        data.setCountry(country == null?"":country);

        String province = userJson.getString("province");
        data.setProvince(province == null?"":province);

        String city = userJson.getString("city");
        data.setCity(city == null?"":city);

        String nickName = userJson.getString("nickName");
        data.setNickName(nickName == null?"":nickName);

        String avatarUrl = userJson.getString("avatarUrl");
        data.setAvatarUrl(avatarUrl == null ?"":avatarUrl);
    }
}