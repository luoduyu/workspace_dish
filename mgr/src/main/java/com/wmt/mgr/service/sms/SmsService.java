package com.wmt.mgr.service.sms;

import com.wmt.commons.domain.packet.BizPacket;

/**
 * Copyright (c) 2019 by CANSHU
 *  短信
 *
 * @author adu Create on 2019-02-21 11:41
 * @version 1.0
 */
public interface SmsService {
    public BizPacket sendSMS(String mobile, int timeoutMinutes, String templateCode);
}
