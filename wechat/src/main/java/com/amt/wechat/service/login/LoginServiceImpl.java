package com.amt.wechat.service.login;

import com.aliyuncs.exceptions.ClientException;
import com.amt.wechat.service.redis.RedisService;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.sms.AliSMS;
import com.amt.wechat.domain.util.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-02 14:54
 * @version 1.0
 */
@Service("loginService")
public class LoginServiceImpl implements  LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    private @Value("${devMode}") boolean devMode;
    private @Resource  RedisService redisService;

    @Override
    public BizPacket sendSMS(String mobile,int timeoutMinutes, String templateCode) {
        String code = WechatUtil.generateCode();
        try {
            AliSMS.send(mobile,code,templateCode);

            // 成功后的事情
            redisService.onSMSSend(mobile, code,timeoutMinutes);

            if(devMode){
                logger.error("手机号={}的朋友登录短信验证码={}",mobile,code);
                return BizPacket.success(code);
            }

            return BizPacket.success("");
        } catch (ClientException e) {
           logger.error("mobile="+mobile+",e="+e.getMessage(),e);
           return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }
}