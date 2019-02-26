package com.wmt.mgr.service.wechat.bidding;

import com.wmt.commons.domain.packet.BizPacket;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *   竞价
 *
 * @author adu Create on 2019-02-26 15:40
 * @version 1.0
 */
public interface BiddingService {

    public BizPacket poiList(String brandName,String masterName,String masterMobile,Integer index,Integer pageSize);
}
