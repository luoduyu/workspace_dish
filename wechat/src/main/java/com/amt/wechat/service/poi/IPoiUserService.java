package com.amt.wechat.service.poi;

import com.amt.wechat.domain.PhoneData;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.model.poi.PoiUserData;

import javax.servlet.http.HttpSession;
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
     * @param phoneData
     * @return
     */
    public BizPacket weichatLogin(String code, String  encryptedData, String iv, PhoneData phoneData);

    /**
     * 获取用户的手机号,并存储至 HttpSession
     * @param session
     * @param code
     * @param encryptedData
     * @param iv
     * @return
     */
    public BizPacket weichatLogin4Phone(HttpSession session,String code, String  encryptedData, String iv);

    /**
     * 手机号认证授权
     * @param name
     * @param mobile
     * @return
     */
    public BizPacket auth4Mobile(String name, String mobile, PoiUserData userData);

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
     * @param id
     * @param name
     * @return
     */
    public BizPacket updatePoiUserName(String id,String name);

    public BizPacket testLogin() throws IOException;
}