package com.wmt.mgr.controller.base;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.commons.domain.sms.AliSMS;
import com.wmt.commons.util.WmtUtil;
import com.wmt.mgr.service.redis.RedisService;
import com.wmt.mgr.service.sms.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-22 11:00
 * @version 1.0
 */
@RestController
public class SmsController extends  BaseController{

    private static Logger logger = LoggerFactory.getLogger(SmsController.class);

    private @Resource RedisService redisService;
    private @Resource SmsService smsService;

    /**
     * 验证码发送 -- 账户类操作
     * @param mobile 手机号
     * @param type 1:短信, 2:语音; 默认为短信类型
     * @return
     */
    @RequestMapping(value="/mgr/sms/auth",method = {RequestMethod.POST,RequestMethod.GET})
    public BizPacket sendSMS4Auth_mobile(String mobile, Integer type) {
        if(type == null){
            type = 1;
        }
        return sendSMS(mobile,type,5, AliSMS.SMS_TEMPLATE_ACCOUNT);
    }


    /**
     * 验证码发送 -- 运营操作类：
     * @param mobile 手机号
     * @param type 1:短信, 2:语音; 默认为短信类型
     * @return
     */
    @RequestMapping(value="/mgr/sms/go",method = {RequestMethod.POST,RequestMethod.GET})
    public BizPacket sendSMS4GO(String mobile, Integer type) {
        if(type == null){
            type = 1;
        }
        return sendSMS(mobile,type,30,AliSMS.SMS_TEMPLATE_OP);
    }

    /**
     *
     * @param type 1:短信, 2:语音; 默认为短信类型
     * @param timeoutMinutes 验证码的有效期(存储时长,单位:分钟)
     * @return
     */
    private BizPacket sendSMS(String mobile,int type,int timeoutMinutes,String templateCode) {
        if(StringUtils.isEmpty((mobile))){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"请输入手机号");
        }

        if (!WmtUtil.isMobileNO(mobile)) {
            return BizPacket.error(HttpStatus.PAYMENT_REQUIRED.value(), "手机号格式不正确!");
        }

        try {
            // 60秒锁验证
            boolean lock = redisService.canSMSSendMinute(mobile);
            if (!lock) {
                return BizPacket.error(HttpStatus.PAYMENT_REQUIRED.value(), "请稍等，1分钟只能发送一次验证码，谢谢!");
            }

            // 每日15次上限验证
            long count = redisService.getSMSSendAmountToday(mobile);
            if (count >= 16) {
                return BizPacket.error(HttpStatus.PAYMENT_REQUIRED.value(), "您好，每个手机号每天只能登录15次，谢谢!");
            }

            BizPacket packet= smsService.sendSMS(mobile,timeoutMinutes, templateCode);
            return packet;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),"服务器忙,请稍后重试!");
        }
    }
}