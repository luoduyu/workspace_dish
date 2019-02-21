package com.wmt.wechat.service.go;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.wechat.form.yunying.ShenQingForm;
import com.wmt.wechat.model.poi.PoiUserData;

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
    public BizPacket requestFormSubmit(ShenQingForm form, PoiUserData userData, ShenQingType userFor);


    /**
     *
     * @param userData
     * @param usefor 0:开店申请;1:运营申请
     * @return
     */
    public BizPacket requestFormGet(PoiUserData userData,ShenQingType usefor);

    public BizPacket requestFormReSubmit(PoiUserData userData,int id);
}
