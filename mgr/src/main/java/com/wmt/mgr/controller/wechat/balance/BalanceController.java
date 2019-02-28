package com.wmt.mgr.controller.wechat.balance;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.domain.annotation.PostMappingEx;
import com.wmt.mgr.domain.rabc.permission.MgrModules;
import com.wmt.mgr.service.wechat.balance.BalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Copyright (c) 2019 by CANSHU
 * <p>
 * 账户收支业务
 *
 * @author lujunp Create on 2019/2/28 14:58
 * @version 1.0
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class BalanceController {

    private static final Logger logger = LoggerFactory.getLogger(BalanceController.class);

    @Resource
    private BalanceService balanceService;

    @PostMappingEx(value = "/mgr/balance/consume/detail", funcName = "账户消费详情", module = MgrModules.BALANCE)
    public BizPacket queryBalanceConsumeDetail(String orderId) {

        if (orderId == null || orderId.length() <= 0) {
            return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(), "订单号不能为空orderId=" + orderId);
        }
        return balanceService.getBalanceConsumeDetail(orderId);
    }

    @PostMappingEx(value = "/mgr/balance/recharge/detail",funcName = "账户充值详情",module = MgrModules.BALANCE)
    public BizPacket queryBalanceRechargeDetail(String orderId){
        if(orderId == null || orderId.length() <= 0){
            return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(),"订单号不能为空orderId=" + orderId);
        }
        return balanceService.getBalanceRechargeDetail(orderId);
    }


}
