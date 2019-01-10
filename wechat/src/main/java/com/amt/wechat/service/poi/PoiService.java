package com.amt.wechat.service.poi;

import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.form.basic.BasicSettingForm;
import com.amt.wechat.model.poi.MaterialData;
import com.amt.wechat.model.poi.PoiMemberData;
import com.amt.wechat.model.poi.PoiUserData;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  店铺相关的服务
 *
 * @author adu Create on 2019-01-03 20:21
 * @version 1.0
 */
public interface PoiService {

    /**
     * 店铺的装修材料
     * @return 数据以 MaterialData.showSeq 升序排列
     */
    public List<MaterialData> getPoiMaterialDataList();

    /**
     * 更新店铺信息
     * @param userData
     * @param form
     * @return
     */
    public BizPacket updatePoi(PoiUserData userData,BasicSettingForm form);


    /**
     * 会员数据获取
     * @return
     */
    public BizPacket memberDataFetch(PoiUserData userData);

    /**
     * 指定店铺是否会员
     * @param poiId
     * @return
     */
    public boolean isMember(String poiId);

    /**
     * 指定店铺是否会员
     *
     * @param memberData
     * @return
     */
    public boolean isMember(PoiMemberData memberData);


    /**
     * 会员卡购买记录
     * @param index
     * @param pageSize
     * @return
     */
    public BizPacket memberBoughtRD(PoiUserData userData,int index,int pageSize);

    /**
     * 会员购买
     * @param userData
     * @param memberCardId
     * @param feeRenew
     * @return
     */
    public BizPacket memberBuy(PoiUserData userData,int memberCardId,int feeRenew);
}
