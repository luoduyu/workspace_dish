package com.wmt.mgr.service.wechat.poi;

import com.wmt.commons.domain.packet.BizPacket;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author lujunp Create on 2019/2/28 13:50
 * @version 1.0
 */
public interface PoiAccountService {

    /**
     * 根据条件查询商户账户余额列表
     * @param branchName        品牌名称
     * @param poiUserName       店铺负责人
     * @param poiUserMobile     店铺负责人手机
     * @param index             起始页
     * @param pageSize          数据量/页
     * @return                  商户账户余额列表
     */
    public BizPacket queryPoiAccountList(String branchName, String poiUserName,
                                         String poiUserMobile,int index, int pageSize);

}
