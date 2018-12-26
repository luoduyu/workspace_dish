package com.amt.wechat.service.poi;

import com.amt.wechat.domain.packet.BizPacket;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-17 10:38
 * @version 1.0
 */
public interface IPOIUserService {

    public BizPacket weichatLogin(String code, String  encryptedData, String iv);
}
