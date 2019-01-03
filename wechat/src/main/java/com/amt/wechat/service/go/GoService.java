package com.amt.wechat.service.go;

import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.form.ShenQingForm;
import com.amt.wechat.model.poi.PoiUserData;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  运营类
 * @author adu Create on 2019-01-02 19:34
 * @version 1.0
 */
public interface GoService {

    /**
     * 开店/运营申请提交
     * @param form
     * @param userData
     * @return
     */
    public BizPacket requestFormSubmit(ShenQingForm form, PoiUserData userData);


    /**
     *
     * @param userData
     * @param usefor 0:开店申请;1:运营申请
     * @return
     */
    public BizPacket requestFormGet(PoiUserData userData,int usefor);

    public BizPacket requestFormReSubmit(int id);
}
