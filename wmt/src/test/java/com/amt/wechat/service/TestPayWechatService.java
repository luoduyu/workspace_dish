package com.amt.wechat.service;

import com.amt.wechat.common.constants.Constants;
import com.amt.wechat.domain.id.Generator;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.service.pay.PayWechatService;
import com.amt.wechat.service.pay.util.Sha1Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-23 15:43
 * @version 1.0
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPayWechatService {
    private @Resource  PayWechatService payWechatService;


    @Test
    public void testPreOrder(){

        //String nonce_str = "c5ab6cebaca97f7171139e4d414ff5a6";
        String nonce_str = Sha1Util.getNonceStr();
        String body = "shop-recharge-1fen";

        String orderId = Generator.generate();
        String attach = "biddingRechargeId="+ 8;
        String openId = "o3Gbc4qWdG5sxoKU5BByDVzuM5to";

        //nonce_str=c5ab6cebaca97f7171139e4d414ff5a6,orderId=20190123161796062001
        System.out.println("nonce_str="+nonce_str+",orderId="+orderId);
        BizPacket bizPacket = payWechatService.prePayOrder(openId,nonce_str,body,attach,orderId,1, Constants.PAY_CALLBACK_URL_ALL_BIDDING);
        System.out.println(bizPacket);
    }
}
