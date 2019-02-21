package com.wmt.wechat.service.login;

import com.wmt.commons.domain.packet.BizPacket;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-02 14:52
 * @version 1.0
 */
public interface LoginService {

    /**
     * 发送登录短信验证码
     *
     * @param mobile 手机号
     * @param timeoutMinutes 验证码的有效期(存储时长)
     * @param templateCode 阿里云上的短信模板
     */
    public BizPacket sendSMS(String mobile, int timeoutMinutes, String templateCode);
}
