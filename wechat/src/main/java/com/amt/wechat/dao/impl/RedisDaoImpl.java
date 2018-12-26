package com.amt.wechat.dao.impl;

import com.amt.wechat.common.Constants;
import com.amt.wechat.common.RedisConstants;
import com.amt.wechat.dao.RedisDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.amt.wechat.common.RedisConstants.AccessTokenValidM;

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


    @Override
    public void addAccessToken(String accessToken, String sessionKey,String openid){
        try {
            String key =  RedisConstants.PRE_ACCESS_TOKEN + accessToken;
            String value = sessionKey+"_"+openid;
            stringRedisTemplate.opsForValue().set(key,value, RedisConstants.AccessTokenValidM, TimeUnit.MINUTES);
        } catch (Exception e) {
            logger.info("accessToken="+accessToken+",sessionKey="+sessionKey+",openid="+openid+",e="+e.getMessage(),e);
        }
    }

    @Override
    public String getAccessToken(String accessToken) {
        try {
            String value = stringRedisTemplate.opsForValue().get(RedisConstants.PRE_ACCESS_TOKEN + accessToken);
            if(value == null || value.trim().length() ==0){
                return null;
            }

            // 续时长
            stringRedisTemplate.expire(RedisConstants.PRE_ACCESS_TOKEN + accessToken, RedisConstants.AccessTokenValidM, TimeUnit.MINUTES);
            //return jacksonObjMapper.readValue(value, RegUser.class);
            return value;
        } catch (Exception e) {
            logger.info("accessToken="+accessToken+",e="+e.getMessage(),e);
        }
        return null;
    }

    @Override
    public void set(String key, String value, long duration) {
        //redisTemplate.opsForValue().set(PREFIX_ACCESS_TOKEN + accessToken, user.getId(), accessTokenValidMinutes, TimeUnit.MINUTES);
    }

    @Override
    public void delete(String key) {

    }
}