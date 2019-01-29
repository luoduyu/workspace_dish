package com.amt.wechat.service.redis;

import com.amt.wechat.model.poi.PoiUserData;
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
    private @Autowired StringRedisTemplate stringRedisTemplate;
    private @Autowired RedisTemplate<String, Serializable> redisTemplate;

    public final static DateTimeFormatter DATE_COMPACT = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.of(ZoneId.SHORT_IDS.get("CTT")));


    @Override
    public void addPoiUser(PoiUserData poiUserData) throws IOException {
        String tkey =  RedisConstants.WECHAT_ACCESS_TOKEN + poiUserData.getAccessToken();
        stringRedisTemplate.opsForValue().set(tkey,poiUserData.getId(), RedisConstants.WECHAT_TOKEN_TIMEOUT, TimeUnit.MINUTES);

        String ukey = RedisConstants.WECHAT_POI_USER+poiUserData.getId();
        redisTemplate.opsForHash().put(ukey,poiUserData.getId(),poiUserData);
        redisTemplate.expire(ukey,RedisConstants.WECHAT_TOKEN_TIMEOUT,TimeUnit.MINUTES);
    }

    @Override
    public PoiUserData getPoiUser(String accessToken) {
        try {
            String poiUserId = stringRedisTemplate.opsForValue().get(RedisConstants.WECHAT_ACCESS_TOKEN + accessToken);
            if(poiUserId == null || poiUserId.trim().length() ==0){
                return null;
            }

            String ukey =  RedisConstants.WECHAT_POI_USER+poiUserId;
            Object objUser=  redisTemplate.opsForHash().get(ukey,poiUserId);
            if(objUser == null){
                stringRedisTemplate.delete(RedisConstants.WECHAT_ACCESS_TOKEN + accessToken);
                return null;
            }

            // 续时长
            stringRedisTemplate.expire(RedisConstants.WECHAT_ACCESS_TOKEN + accessToken, RedisConstants.WECHAT_TOKEN_TIMEOUT, TimeUnit.MINUTES);
            redisTemplate.expire(ukey, RedisConstants.WECHAT_TOKEN_TIMEOUT, TimeUnit.MINUTES);

            return (PoiUserData)objUser;
        } catch (Exception e) {
            logger.info("accessToken="+accessToken+",e="+e.getMessage(),e);
        }
        return null;
    }

    @Override
    public PoiUserData getPoiUserById(String poiUserId){
        String ukey =  RedisConstants.WECHAT_POI_USER+poiUserId;
        Object objUser=  redisTemplate.opsForHash().get(ukey,poiUserId);
        if(objUser == null){
            return null;
        }
        return (PoiUserData)objUser;
    }

    @Override
    public void onUserRemoved(String poiUserId){
        String ukey =  RedisConstants.WECHAT_POI_USER+poiUserId;
        Object objUser=  redisTemplate.opsForHash().get(ukey,poiUserId);
        if(objUser == null){
            return;
        }
        redisTemplate.delete(ukey);
    }


    @Override
    public boolean canSMSSendMinute(String mobile){
        try {
            String key = RedisConstants.WECHAT_SMS+mobile;
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
        String key = RedisConstants.WECHAT_SMS_COUNT +"_" + LocalDate.now().format(DATE_COMPACT)+":"+mobile;
        String obj = stringRedisTemplate.opsForValue().get(key);
        return obj==null? 0: Long.parseLong(obj);
    }


    @Override
    public void onSMSSend(String mobile,String smscode,int timeoutMinutes){

        // 验证码的存储
        stringRedisTemplate.opsForValue().set(RedisConstants.WECHAT_SMS_CODE + mobile, smscode, timeoutMinutes, TimeUnit.MINUTES);

        // 60秒锁的施加
        try {
            stringRedisTemplate.opsForValue().setIfAbsent(RedisConstants.WECHAT_SMS+mobile,smscode,60,TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("mobile="+mobile+",smscode="+smscode+",e="+e.getMessage(),e);
        }

        // 当天的次数增1
        try {
            //String k_daycount = RedisConstants.WECHAT_SMS_COUNT + mobile;
            String today = LocalDate.now().format(DATE_COMPACT);
            String k_daycount = RedisConstants.WECHAT_SMS_COUNT +"_" + today+":"+mobile;

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
        return stringRedisTemplate.opsForValue().get(RedisConstants.WECHAT_SMS_CODE + mobile);
    }

    @Override
    public void onSmsVerify(boolean verifyResult,String userId,String mobile){
        if(verifyResult){
            stringRedisTemplate.opsForValue().set(RedisConstants.WECHAT_SMS_RESULT + userId, mobile, 1, TimeUnit.MINUTES);
        }
        else{
            stringRedisTemplate.delete(RedisConstants.WECHAT_SMS_RESULT + userId);
        }
    }

    @Override
    public String getMobile4Forget(String userId){
        String k = RedisConstants.WECHAT_SMS_RESULT + userId;
        String mobile = stringRedisTemplate.opsForValue().get(k);
        stringRedisTemplate.delete(k);
        return mobile;
    }

    @Override
    public void onSnapSucc(String poiId,int cateId, String timeStart){
        // 当天的次数增1
        try {

            String postfix = poiId+"_"+cateId+"_"+timeStart;
            String today = LocalDate.now().format(DATE_COMPACT);
            String k_daycount = RedisConstants.WECHAT_SNAP_COUNT +"_" + today+":"+postfix;

            long count = stringRedisTemplate.opsForValue().increment(k_daycount, 1);
            if(count ==1){
                redisTemplate.expire(k_daycount, 24, TimeUnit.HOURS);
            }

        } catch (Exception e) {
            //如果redis发现异常，仍然返回正常，保证业务继续运行
            logger.error("poiId="+poiId+",cateId="+cateId+",timeStart="+timeStart+",e="+e.getMessage(),e);
        }
    }

    @Override
    public int getSnapNum(String poiId,int cateId, String timeStart){
        String postfix = poiId+"_"+cateId+"_"+timeStart;
        String today = LocalDate.now().format(DATE_COMPACT);
        String k_daycount = RedisConstants.WECHAT_SNAP_COUNT +"_" + today+":"+postfix;

        String obj = stringRedisTemplate.opsForValue().get(k_daycount);
        return obj==null? 0: Integer.parseInt(obj);
    }
}