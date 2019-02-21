package com.wmt.wechat.service.redis.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wmt.commons.constants.RedisConstants;
import com.wmt.wechat.domain.util.WechatUtil;
import com.wmt.wechat.model.balance.BalanceSettingData;
import com.wmt.wechat.model.poi.PoiUserData;
import com.wmt.wechat.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service("redisService")
public class RedisServiceImpl implements RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
    private @Autowired StringRedisTemplate stringRedisTemplate;
    private @Autowired RedisTemplate<String, Serializable> redisTemplate;
    private static BalanceSettingData balanceSettingData;

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

            // accessToken不存在的时候同时删除其对应的 'UserData'
            if(poiUserId == null || poiUserId.trim().length() ==0){
                String ukey = RedisConstants.WECHAT_POI_USER+poiUserId;
                redisTemplate.delete(ukey);
                return null;
            }

            String ukey = RedisConstants.WECHAT_POI_USER+poiUserId;
            Object objUser=  redisTemplate.opsForHash().get(ukey,poiUserId);
            if(objUser == null){
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
        logger.info("删除成员={},has={}",objUser,redisTemplate.hasKey(ukey));
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
    public void onSnapSucc(int cateId, String timeFrameStart){
        // 当天的次数增1
        try {

            String key = RedisConstants.WECHAT_SNAP_COUNT_CATE + LocalDate.now().format(DATE_COMPACT)+":"+cateId+":"+timeFrameStart.replaceAll(":","");

            long count = stringRedisTemplate.opsForValue().increment(key, 1);
            if(count ==1){
                redisTemplate.expire(key, 24, TimeUnit.HOURS);
            }

        } catch (Exception e) {
            //如果redis发现异常，仍然返回正常，保证业务继续运行
            logger.error("cateId="+cateId+",timeFrameStart="+timeFrameStart+",e="+e.getMessage(),e);
        }
    }

    @Override
    public int getSnapNum(int cateId,String timeFrameStart){
        String key = RedisConstants.WECHAT_SNAP_COUNT_CATE + LocalDate.now().format(DATE_COMPACT)+":"+cateId+":"+timeFrameStart.replaceAll(":","");

        String obj = stringRedisTemplate.opsForValue().get(key);
        return obj==null? 0: Integer.parseInt(obj);
    }


    @Override
    public Map<String,Integer> countTodaySnapSoldNum(int cateId){
        String pattern = RedisConstants.WECHAT_SNAP_COUNT_CATE + LocalDate.now().format(DATE_COMPACT)+":"+cateId+":";

        RedisConnection connection = stringRedisTemplate.getConnectionFactory().getConnection();
        ScanOptions options = ScanOptions.scanOptions().match(pattern+"*").build();
        Cursor<byte[]> cursor = connection.scan(options);


        Map<String,Integer> result = new HashMap<>();
        while (cursor.hasNext()) {
            String key = new String(cursor.next());

            String value = stringRedisTemplate.opsForValue().get(key);
            //result.put(key,value);
            //System.out.println(key+"="+value);
            if(value  == null){
                continue;
            }
            String hhMMssTimeFrameStart = key.replace(pattern,"");
            result.put(hhMMssTimeFrameStart,Integer.parseInt(value));
        }
        //System.out.println("total="+total);
        try {
            cursor.close();
        } catch (Exception e) {
            logger.error("key="+pattern+",e="+e.getMessage(),e);
        }
        try {
            logger.info("connection.isClosed():{}",connection.isClosed());
            RedisConnectionUtils.releaseConnection(connection, redisTemplate.getConnectionFactory());
        } catch (Exception e) {
            logger.error("key="+pattern+",e="+e.getMessage(),e);
        }
        return result;
    }

    @Override
    public void onBalanceSettingChanged(String settingJSONMessage) {
        logger.info("收到余额相关设置数据={}",settingJSONMessage);
        BalanceSettingData _temp = JSON.parseObject(settingJSONMessage,BalanceSettingData.class);;
        balanceSettingData = _temp;
    }

    @Override
    public BalanceSettingData getBalanceSetting(){
        return balanceSettingData;
    }

    @Override
    public String getWeixinAccessToken() throws IOException {
        String accessToken = stringRedisTemplate.opsForValue().get(RedisConstants.REDIS_WECHAT_ACCESS_TOKEN);
        if(accessToken != null){
            return accessToken;
        }

        long now = System.currentTimeMillis();

        JSONObject jsonObject = WechatUtil.getWeixinAccessToken();
        if(jsonObject == null){
            return null;
        }
        accessToken  = jsonObject.getString("access_token");
        if(accessToken == null){
            throw new IOException(jsonObject.getString("errmsg"));
        }

        // 计算从请求发起，到获得响应应的时间差,并冗余1秒
        int delta = (int)((System.currentTimeMillis() - now)/1000 + 1);
        Integer expires_in = jsonObject.getInteger("expires_in");

        // 为确保可靠,必须减掉请求时间
        if(expires_in != null){
            expires_in -= delta;
        }else{

            // 官方值为7200,为确保可靠,设置个短一些的时长
            expires_in = 7000;
        }
        stringRedisTemplate.opsForValue().setIfAbsent(RedisConstants.REDIS_WECHAT_ACCESS_TOKEN,accessToken.trim(),expires_in,TimeUnit.SECONDS);
        return accessToken;
    }
}