package com.amt.wechat.service.balance;

import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.model.balance.CurrencyStageData;
import com.amt.wechat.model.order.OrderData;
import com.amt.wechat.model.poi.PoiMemberRDData;
import com.amt.wechat.model.poi.PoiUserData;

import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2019 by CANSHU
 *  帐户余额相关业务
 *
 * @author adu Create on 2019-01-12 11:18
 * @version 1.0
 */
public interface BalanceService {


    /**
     * 充值面额档位数据
     * @return
     */
    public List<CurrencyStageData> getStageDataList();

    /**
     * 余额充值
     * @param userData
     * @param amount
     * @return
     */
    public BizPacket recharge(PoiUserData userData, int amount) throws Exception;

    /**
     * 余额充值微信回调
     * @param wechatPayCallbackParams
     * @return
     */
    public BizPacket rechargeCallback(Map<String,String> wechatPayCallbackParams) throws InterruptedException;


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
     * 余额帐户购买会员
     * @param userData
     * @param rd
     * @return
     * @throws Exception
     */
    public BizPacket memberBuy(PoiUserData userData, PoiMemberRDData rd) throws Exception;

    public BizPacket onOrderPayConfirm(PoiUserData userData, OrderData orderData) throws Exception;
}
