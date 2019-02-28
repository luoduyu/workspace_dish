package com.wmt.mgr.service.wechat.balance.impl;

import com.alibaba.fastjson.JSONObject;
import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.dao.balance.BalanceDao;
import com.wmt.mgr.model.balance.BalanceConsumeData;
import com.wmt.mgr.model.balance.BalanceRechargeData;
import com.wmt.mgr.service.wechat.balance.BalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@Service("balanceConsumeServiceImpl")
public class BalanceServiceImpl implements BalanceService {

    @Resource
    private BalanceDao balanceDao;

    @Override
    public BizPacket getBalanceConsumeDetail(@NotNull String orderId) {

        BalanceConsumeData balanceConsumeData = balanceDao.selectBalanceConsumeByOrderId(orderId);
        if (balanceConsumeData == null) {
            return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(), "未找到订单号对应基本信息，orderId=" + orderId);
        }
        return BizPacket.success(balanceConsumeData);
    }

    @Override
    public BizPacket getBalanceRechargeDetail(@NotNull String orderId) {
        BalanceRechargeData balanceRechargeData = balanceDao.selectBalanceRechargeByOrderId(orderId);

        if(balanceRechargeData == null){
            return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(),"未找到订单号对应基本信息，orderId=" + orderId);
        }
        return BizPacket.success(balanceRechargeData);
    }
}
