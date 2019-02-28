package com.wmt.mgr.service.wechat.balance;

import com.wmt.commons.domain.packet.BizPacket;

import javax.validation.constraints.NotNull;

/**
 * Copyright (c) 2019 by CANSHU
 * 商户账户余额消费业务
 *
 * @author lujunp Create on 2019/2/28 15:09
 * @version 1.0
 */
public interface BalanceService {

    /**
     * 根据订单号查询账户的消费详情
     *
     * @param orderId       订单号
     * @return              账户消费详情
     */
    public BizPacket getBalanceConsumeDetail(@NotNull String orderId);

    /**
     * 根据订单号查询账户的充值详情
     * @param orderId       订单号
     * @return              账户充值详情
     */
    public BizPacket getBalanceRechargeDetail(@NotNull String orderId);

}
