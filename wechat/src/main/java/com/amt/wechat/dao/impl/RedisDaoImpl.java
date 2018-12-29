package com.amt.wechat.dao.impl;

import com.amt.wechat.common.RedisConstants;
import com.amt.wechat.dao.RedisDao;
import com.amt.wechat.model.poi.POIUserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-18 19:23
 * @version 1.0
 */
@Service("redisDao")
public class RedisDaoImpl implements RedisDao {

    private static final Logger logger = LoggerFactory.getLogger(RedisDaoImpl.class);
    private @Autowired StringRedisTemplate stringRedisTemplate;
    private @Autowired RedisTemplate<String, Serializable> redisTemplate;


    @Override
    public void addPOIUser(POIUserData poiUserData) throws IOException {
        String tkey =  RedisConstants.PRE_ACCESS_TOKEN + poiUserData.getAccessToken();
        stringRedisTemplate.opsForValue().set(tkey,poiUserData.getId(), RedisConstants.TOKEN_TIMEOUT, TimeUnit.MINUTES);

        String ukey = RedisConstants.PREFIX_POI_USER+poiUserData.getId();
        redisTemplate.opsForHash().put(ukey,poiUserData.getId(),poiUserData);
        redisTemplate.expire(ukey,RedisConstants.TOKEN_TIMEOUT,TimeUnit.MINUTES);
    }

    @Override
    public POIUserData getPOIUser(String accessToken) {
        try {
            String poiUserId = stringRedisTemplate.opsForValue().get(RedisConstants.PRE_ACCESS_TOKEN + accessToken);
            if(poiUserId == null || poiUserId.trim().length() ==0){
                return null;
            }

            String ukey =  RedisConstants.PREFIX_POI_USER+poiUserId;
            Object objUser=  redisTemplate.opsForHash().get(ukey,poiUserId);
            if(objUser == null){
                return null;
            }

            // 续时长
            stringRedisTemplate.expire(RedisConstants.PRE_ACCESS_TOKEN + accessToken, RedisConstants.TOKEN_TIMEOUT, TimeUnit.MINUTES);
            redisTemplate.expire(ukey, RedisConstants.TOKEN_TIMEOUT, TimeUnit.MINUTES);

            return (POIUserData)objUser;
        } catch (Exception e) {
            logger.info("accessToken="+accessToken+",e="+e.getMessage(),e);
        }
        return null;
    }

    @Override
    public void delete(String key) {

    }
}