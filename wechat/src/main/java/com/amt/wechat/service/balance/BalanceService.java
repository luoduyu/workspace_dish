package com.amt.wechat.service.balance;

import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.model.poi.PoiUserData;

/**
 * Copyright (c) 2019 by CANSHU
 *  帐户余额相关业务
 *
 * @author adu Create on 2019-01-12 11:18
 * @version 1.0
 */
public interface BalanceService {
    /**
     * 余额充值
     * @param userData
     * @param amount
     * @return
     */
    public BizPacket recharge(PoiUserData userData, int amount);


    /**
     * 充值记录
     * @param userData
     * @param index
     * @param pageSize
     * @return
     */
    public BizPacket getMyRechargeData(PoiUserData userData, int index, int pageSize);

    /**
     * 消费记录
     * @param userData
     * @param index
     * @param pageSize
     * @return
     */
    public BizPacket getMyConsumeData(PoiUserData userData, int index, int pageSize);
}
