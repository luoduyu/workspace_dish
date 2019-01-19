package com.amt.wechat.service.poi;

import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.form.basic.WeichatLoginForm;
import com.amt.wechat.model.poi.PoiUserData;

import java.io.IOException;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-17 10:38
 * @version 1.0
 */
public interface IPoiUserService {

    /**
     * 根据授权个人微信信息和存储至HttpSession中的手机号注册(若未注册)并登录
     * @param code
     * @param encryptedData
     * @param iv
     * @return
     */
    public BizPacket weichatLogin(String code, String  encryptedData, String iv);

    /**
     * 手机号认证授权
     * @param name
     * @param mobile
     * @return
     */
    public BizPacket auth4Boss(String name, String mobile, PoiUserData userData);
    public BizPacket auth4Empl(String name, String mobile, PoiUserData userData);

    /**
     * 手机号更换
     * @param currUserData 当前用户信息
     *
     * @param newMobile 新手机号
     *
     * @return
     */
    public BizPacket mobileReplace(PoiUserData currUserData, String newMobile);

    /**
     * 姓名更新
     * @param userData
     * @param name
     * @return
     */
    public BizPacket updatePoiUserName(PoiUserData userData,String name);

    public BizPacket login4mobile(String mobile) throws IOException;

    public WeichatLoginForm buildResponse(PoiUserData userData);

    /**
     * 获取店员列表
     *
     * @param userData
     * @return
     */
    public BizPacket employeeList(PoiUserData userData);
}