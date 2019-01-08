package com.amt.wechat.service.bidding;

import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.model.bidding.BiddingStageData;
import com.amt.wechat.model.poi.PoiUserData;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-08 17:13
 * @version 1.0
 */
public interface BiddingService {

    public List<BiddingStageData> getStageDataList();


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


    /**
     * 竞价充值
     * @param userData
     * @param amount
     * @return
     */
    public BizPacket recharge(PoiUserData userData,int amount);
}
