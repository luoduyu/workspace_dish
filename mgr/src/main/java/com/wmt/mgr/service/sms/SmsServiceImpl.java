package com.wmt.mgr.service.sms;


import com.aliyuncs.exceptions.ClientException;
import com.wmt.commons.domain.id.Generator;
import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.commons.domain.sms.AliSMS;
import com.wmt.mgr.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("smsService")
public class SmsServiceImpl implements SmsService {
    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);

    /**
     * 是否 开发模式
     */
    private @Value("${devMode}") boolean devMode;
    private @Resource RedisService redisService;

    @Override
    public BizPacket sendSMS(String mobile, int timeoutMinutes, String templateCode) {
        String code = Generator.generateCode();
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
            logger.error("mobile="+mobile+",code="+code+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }
}
