package com.wmt.mgr.service.redis.impl;

import com.wmt.commons.constants.RedisConstants;
import com.wmt.mgr.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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
}
