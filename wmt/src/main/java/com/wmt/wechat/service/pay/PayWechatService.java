package com.wmt.wechat.service.pay;

import com.wmt.commons.domain.packet.BizPacket;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  微信支付
 * @author adu Create on 2019-01-21 18:53
 * @version 1.0
 */
public interface PayWechatService {

    /**
     * 生成微信预支付订单
     * @param openId
     * @param nonce_str 随机字符串，长度要求在32位以内。推荐随机数生成算法
     * @param body 商品简单描述
     * @param attach  = ?
     * @param orderNo  商户方生成的订单号
     * @param amount  支付金额,单位:分
     * @param notifyCallbackUrl	支付回调URL
     *
     * @return 预支付的微信订单号
     *
     */
    public BizPacket prePayOrder(String openId, String nonce_str, String body, String attach, String orderNo, int amount, String notifyCallbackUrl);
}
