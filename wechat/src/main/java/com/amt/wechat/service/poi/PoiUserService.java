package com.amt.wechat.service.poi;

import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.form.basic.WeichatLoginForm;
import com.amt.wechat.model.poi.PoiCandidate;
import com.amt.wechat.model.poi.PoiMemberRDData;
import com.amt.wechat.model.poi.PoiUserData;

import java.io.IOException;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * 店铺成员业务
 *
 * @author adu Create on 2018-12-17 10:38
 * @version 1.0
 */
public interface PoiUserService {

    /**
     * 根据授权个人微信信息和存储至HttpSession中的手机号注册(若未注册)并登录
     * @param code
     * @param encryptedData
     * @param iv
     * @return
     */
    public BizPacket weichatLogin(String code, String  encryptedData, String iv,String inviterId);

    /**
     * 手机号认证授权
     * @param name
     * @param mobile
     * @return
     */
    public BizPacket auth4Boss(String name, String mobile, PoiUserData userData);

    /**
     * 更新个人姓名和手机号
     * @param name
     * @param mobile
     * @param userData
     * @return
     */
    public BizPacket employeeSet(String name, String mobile, PoiUserData userData);
    public BizPacket poiBind(PoiUserData userData, PoiCandidate candidate);

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

    /**
     * 店铺老枪邀请店员认领店铺
     * @param userData
     * @param name
     * @param mobile
     * @return
     */
    public BizPacket bossInviteIn(PoiUserData userData,String name,String mobile);

    /**
     * 转让老板
     * @param boss
     * @param userId
     * @return
     */
    public BizPacket bossTransferTo(PoiUserData boss,String userId);


    /**
     * 店员删除
     * @param boss
     * @param userId
     * @return
     */
    public BizPacket employeeRM(PoiUserData boss,String userId);


    /**
     *  邀请成功
     *
     * @param inviterId 邀请者Id
     */
    public void onInviteSucc(String inviterId,String inviteeId, PoiMemberRDData rd);
}