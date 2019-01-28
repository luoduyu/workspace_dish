package com.amt.wechat.service.snap;

import com.amt.wechat.domain.packet.BizPacket;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *   抢购业务
 *
 * @author adu Create on 2019-01-28 15:07
 * @version 1.0
 */
public interface SnapService {


    public BizPacket todaySnapGoodsList(int cateId);
}
