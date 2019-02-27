package com.wmt.mgr.service.wechat.order;

import com.wmt.commons.domain.packet.BizPacket;

/**
 * Copyright (c) 2019 by CANSHU
 * 订单业务接口
 *
 * @author lujunp Create on 2019/2/25 19:53
 * @version 1.0
 */
public interface OrderService {

    /**
     * 获取商品订单列表
     * @param orderId       订单编号
     * @param submitUserMobile  付款人手机号
     * @param poiName       店铺名称
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param index         起始页
     * @param pageSize      count
     * @return      查询结果
     */
    public BizPacket orderList(String orderId, String submitUserMobile, String poiName, String startTime,String endTime, int index,int pageSize);

    /**
     * 通过订单id获取订单信息
     * @param orderId       订单id
     * @return              订单详细信息
     */
    public BizPacket getOrderItemByOrderId(String orderId);

}
