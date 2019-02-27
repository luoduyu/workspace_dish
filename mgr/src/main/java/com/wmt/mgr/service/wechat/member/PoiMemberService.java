package com.wmt.mgr.service.wechat.member;

import com.wmt.commons.domain.packet.BizPacket;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 * 会员订单处理业务
 *
 * @author lujunp Create on 2019/2/27 13:59
 * @version 1.0
 */
public interface PoiMemberService {

    /**
     * 根据查询条件获取会员列表
     *
     * @param orderId           会员订单号
     * @param userMobile        买主手机
     * @param poiName           店铺名称
     * @param startTime         间隔开始时间
     * @param endTime           间隔结束时间
     * @param index             起始页码
     * @param pageSize          数据量/页
     * @return                  查询结果
     */
    public BizPacket getPoiMemberList(String orderId,String userMobile,
                                      String poiName, String startTime,
                                      String endTime, int index,
                                      int pageSize);

    /**
     * 根据会员订单号获取订单信息
     * @param orderId           订单号
     * @return                  订单结果
     */
    public BizPacket getPoiMemberByOrderId(String orderId);

}
