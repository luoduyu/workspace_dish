package com.wmt.mgr.domain.redis;

import com.wmt.mgr.dao.balance.BalanceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Copyright (c) 2019 by CANSHU
 *  消息发布者
 * @author adu Create on 2019-02-01 13:29
 * @version 1.0
 */

@EnableScheduling
@Component
public class MessageSender {
    private static Logger logger = LoggerFactory.getLogger(MessageSender.class);

    private @Resource StringRedisTemplate stringRedisTemplate;
    private @Resource BalanceDao balanceDao;

    /**
     * 最后更新时间
     */
    private long updatetime = -1;

//    /**
//     * 间隔10s检查一次 通过StringRedisTemplate对象向redis消息队列频道发布消息
//     */
//    @Scheduled(initialDelay=7000,fixedRate = 10000)
//    public void updateBalanceSetting(){
//        BalanceSettingData settingData =null;
//        try {
//            settingData = balanceDao.getGlobalSettingData();
//        } catch (Exception e) {
//           return;
//        }
//
//        try {
//
//            if(settingData == null){
//                return;
//            }
//            if(updatetime == settingData.getUpdateTime().getTime()){
//                return;
//            }
//            this.updatetime = settingData.getUpdateTime().getTime();
//
//            logger.info("余额相关设置有变化!setting={}",settingData);
//
//            stringRedisTemplate.convertAndSend(RedisConstants.REDIS_CH_BALANCE_SETTING,settingData.toString());
//
//            logger.info("余额相关设置通知完毕!setting={}",settingData.toString());
//        }catch (Exception ex){
//            logger.error(ex.getMessage(), ex);
//        }
//    }
}
