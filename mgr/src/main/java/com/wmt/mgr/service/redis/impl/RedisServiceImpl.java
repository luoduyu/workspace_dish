package com.wmt.mgr.service.redis.impl;

import com.wmt.commons.constants.RedisConstants;
import com.wmt.mgr.model.user.MgrUserData;
import com.wmt.mgr.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Service("redisService")
public class RedisServiceImpl implements RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
    private @Autowired  StringRedisTemplate stringRedisTemplate;
    private @Autowired  RedisTemplate<String, Serializable> redisTemplate;

    private final static DateTimeFormatter DATE_COMPACT = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.of(ZoneId.SHORT_IDS.get("CTT")));


    @Override
    public boolean canSMSSendMinute(String mobile){
        try {
            String key = RedisConstants.MGR_SMS+mobile;
            if(stringRedisTemplate.hasKey(key)){
                return false;
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public long getSMSSendAmountToday(String mobile){
        String key = RedisConstants.MGR_SMS_COUNT +"_" + LocalDate.now().format(DATE_COMPACT)+":"+mobile;
        String obj = stringRedisTemplate.opsForValue().get(key);
        return obj==null? 0: Long.parseLong(obj);
    }


    @Override
    public void onSMSSend(String mobile,String smscode,int timeoutMinutes){

        // 验证码的存储
        stringRedisTemplate.opsForValue().set(RedisConstants.MGR_SMS_CODE + mobile, smscode, timeoutMinutes, TimeUnit.MINUTES);

        // 60秒锁的施加
        try {
            stringRedisTemplate.opsForValue().setIfAbsent(RedisConstants.MGR_SMS+mobile,smscode,60,TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("mobile="+mobile+",smscode="+smscode+",e="+e.getMessage(),e);
        }

        // 当天的次数增1
        try {
            //String k_daycount = RedisConstants.WECHAT_SMS_COUNT + mobile;
            String today = LocalDate.now().format(DATE_COMPACT);
            String k_daycount = RedisConstants.MGR_SMS_COUNT +"_" + today+":"+mobile;

            // 从redis缓存中查看此key使用的次数
            long count = stringRedisTemplate.opsForValue().increment(k_daycount, 1);
            if(count ==1){
                redisTemplate.expire(k_daycount, 24, TimeUnit.HOURS);
            }

        } catch (Exception e) {
            //如果redis发现异常，仍然返回正常，保证业务继续运行
            logger.error("mobile="+mobile+",smscode="+smscode+",e="+e.getMessage(),e);
        }
    }

    @Override
    public String getSMSCode(String mobile){
        return stringRedisTemplate.opsForValue().get(RedisConstants.MGR_SMS_CODE + mobile);
    }

    @Override
    public void addMgrUser(MgrUserData mgrUserData, String oldAccessToken) throws IOException {
        if(oldAccessToken != null && oldAccessToken.trim().length() >= 1){
            String tOldKey= RedisConstants.MGR_ACCESS_TOKEN + oldAccessToken;
            stringRedisTemplate.delete(tOldKey);
        }

        String tkey =  RedisConstants.MGR_ACCESS_TOKEN + mgrUserData.getAccessToken();
        stringRedisTemplate.opsForValue().set(tkey,mgrUserData.getId().toString(), RedisConstants.MGR_TOKEN_TIMEOUT, TimeUnit.MINUTES);

        String ukey = RedisConstants.MGR_USER +mgrUserData.getId();
        redisTemplate.opsForHash().put(ukey,mgrUserData.getId().toString(),mgrUserData);
        redisTemplate.expire(ukey,RedisConstants.MGR_TOKEN_TIMEOUT,TimeUnit.MINUTES);
    }

    @Override
    public MgrUserData getMgrUser(String accessToken) {
        try {
            String poiUserId = stringRedisTemplate.opsForValue().get(RedisConstants.MGR_ACCESS_TOKEN + accessToken);

            // accessToken不存在的时候同时删除其对应的 'UserData'
            if(poiUserId == null || poiUserId.trim().length() ==0){
                String ukey = RedisConstants.MGR_USER +poiUserId;
                redisTemplate.delete(ukey);
                return null;
            }

            String ukey = RedisConstants.MGR_USER +poiUserId;
            Object objUser=  redisTemplate.opsForHash().get(ukey,poiUserId);
            if(objUser == null){
                return null;
            }

            // 续时长
            stringRedisTemplate.expire(RedisConstants.MGR_ACCESS_TOKEN + accessToken, RedisConstants.MGR_TOKEN_TIMEOUT, TimeUnit.MINUTES);
            redisTemplate.expire(ukey, RedisConstants.MGR_TOKEN_TIMEOUT, TimeUnit.MINUTES);

            return (MgrUserData)objUser;
        } catch (Exception e) {
            logger.info("accessToken="+accessToken+",e="+e.getMessage(),e);
        }
        return null;
    }

    @Override
    public MgrUserData getMgrUserById(String mgrUserId){
        String ukey =  RedisConstants.MGR_USER +mgrUserId;
        Object objUser=  redisTemplate.opsForHash().get(ukey,mgrUserId);
        if(objUser == null){
            return null;
        }
        return (MgrUserData)objUser;
    }

    @Override
    public void onUserRemoved(String mgrUserId){
        String ukey =  RedisConstants.MGR_USER +mgrUserId;
        Object objUser=  redisTemplate.opsForHash().get(ukey,mgrUserId);
        if(objUser == null){
            return;
        }
        redisTemplate.delete(ukey);
        logger.info("删除成员={},has={}",objUser,redisTemplate.hasKey(ukey));
    }
}
