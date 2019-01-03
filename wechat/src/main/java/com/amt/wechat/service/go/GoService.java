package com.amt.wechat.service.go;

import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.form.OperationalForm;
import com.amt.wechat.model.poi.POIUserData;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  运营类
 * @author adu Create on 2019-01-02 19:34
 * @version 1.0
 */
public interface GoService {

    public BizPacket formSubmit(OperationalForm form, POIUserData userData);

    public BizPacket formGet(POIUserData userData);

    public BizPacket formReSubmit(int id);
}
