package com.wmt.mgr.service.wechat.poi;

import com.wmt.commons.domain.packet.BizPacket;

/**
 * Copyright (c) 2019 by CANSHU
 * 店铺申请业务处理层
 *
 * @author lujunp Create on 2019/2/27 17:57
 * @version 1.0
 */
public interface PoiRequestService {

    /**
     * 查看店铺申请列表
     * @param id                服务单编号
     * @param poiUserName       店铺人员
     * @param poiUserMobile     人员手机号
     * @param brandName         品牌名称
     * @param province          省
     * @param city              城
     * @param districts         区
     * @param startTime         间隔查询开始时间
     * @param endTime           间隔查询结束时间
     * @param index             起始页
     * @param pageSize          数据量/页
     * @return                  店铺申请列表
     */
    public BizPacket queryPoiRequestList(Integer id,String poiUserName,String poiUserMobile,
                                         String brandName,String province,
                                         String city, String districts, String startTime,
                                         String endTime, int index, int pageSize);


    /**
     * 查询店铺申请详情
     * @param id        服务单编号
     * @return          详情信息
     */
    public BizPacket queryPoiRequestDetail(int id);

}
