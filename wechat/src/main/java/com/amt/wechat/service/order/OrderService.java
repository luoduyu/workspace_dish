package com.amt.wechat.service.order;

import com.amt.wechat.common.PayWay;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.form.order.OrderItemForm;
import com.amt.wechat.form.order.OrderSubmitForm;
import com.amt.wechat.model.poi.PoiUserData;

import java.util.Map;

/**
 * Copyright (c) 2019 by CANSHU
 * 订单类业务
 *
 * @author adu Create on 2019-01-04 19:25
 * @version 1.0
 */
public interface OrderService {

    /**
     * 数据以下单时间倒序排列
     * @param userData
     * @param index 0表示第1页,1表示第2页
     * @param pageSize
     * @return
     */
    public BizPacket getOrderDataList(PoiUserData userData, int index, int pageSize);

    /**
     * 某订单明细
     * @param orderId 订单Id
     * @return
     */
    public BizPacket getOrderDetail(PoiUserData userData,String orderId);


    /**
     * 服务评价
     * @return
     */
    public BizPacket commentSubmit(String orderId, Integer scoreService, Integer scoreProfess, Integer scoreResponse, String commentText, PoiUserData userData);


    /**
     * 订单提交
     * @param userData
     * @param orderSubmitForm
     * @return
     */
    public BizPacket orderSubmit(PoiUserData userData, OrderSubmitForm orderSubmitForm);

    /**
     * 订单数量编辑
     * @return
     */
    public BizPacket orderEditNum( PoiUserData userData,OrderItemForm orderItemForm);

    /**
     * 订单项增加
     * @param orderId
     * @param goodsType 1:海报;2:装修服务
     * @param goodsId
     * @param num
     * @return
     */
    public BizPacket orderItemAdd(PoiUserData userData,String orderId, int goodsType,Integer goodsId, int num);

    /**
     *
     *
     * @param orderId
     * @param goodsId
     * @param orderItemId
     * @return
     */
    public BizPacket orderItemRM(PoiUserData userData,String orderId,Integer goodsId, int orderItemId);


    /**
     * 订单删除
     * @param userData
     * @param orderId
     * @return
     */
    public BizPacket orderRM(PoiUserData userData,String orderId);

    public BizPacket payConfirm(PoiUserData userData, String orderId, PayWay payWay) throws Exception;
    public BizPacket payCallback(Map<String,String> wechatPayCallbackParams);
}