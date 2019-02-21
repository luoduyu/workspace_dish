package com.wmt.wechat.service.poi;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.wechat.form.basic.BasicSettingForm;
import com.wmt.wechat.model.poi.PoiUserData;

import java.io.IOException;

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
     * 更新店铺信息
     * @param userData
     * @param form
     * @return
     */
    public BizPacket updatePoi(PoiUserData userData, BasicSettingForm form);


    /**
     * 余额密码设置
     * @param userData
     * @param pwd
     * @return
     */
    public BizPacket balancePwdSet(PoiUserData userData,String pwd);


    /**
     *
     * @param userData
     * @param flag 是否免密;0:否,1:是
     * @param payPwd
     * @return
     */
    public BizPacket balancePwdRequired(PoiUserData userData, int flag, String payPwd);


    /**
     * 忘记余额密码
     * @param userData
     * @param newPwd
     * @return
     */
    public BizPacket forgetReset(PoiUserData userData,String newPwd);


    /**
     * 余额密码重置
     * @param userData
     * @param oldPwd
     * @param newPwd
     * @return
     */
    public BizPacket balancePwdReset(PoiUserData userData,String oldPwd,String newPwd);


    public BizPacket eleAuth(PoiUserData userData,String accountName,String accountPwd);
    public BizPacket mtAuth(PoiUserData userData,String accountName,String accountPwd);

    /**
     * 微信二维码图片生成
     * @return
     */
    public BizPacket getWXacodeunlimit(PoiUserData userData, String shareUrl, String r, String g, String b) throws IOException;
}