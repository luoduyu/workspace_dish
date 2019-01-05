package com.amt.wechat.service.poi;

import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.form.BasicSettingForm;
import com.amt.wechat.model.poi.MaterialData;
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
     *
     * @return 数据以 MaterialData.showSeq 升序排列
     */
    public List<MaterialData> getPoiMaterialDataList();

    public BizPacket updatePoi(PoiUserData userData,BasicSettingForm form);
}
