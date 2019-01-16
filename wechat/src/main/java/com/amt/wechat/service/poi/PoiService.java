package com.amt.wechat.service.poi;

import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.form.basic.BasicSettingForm;
import com.amt.wechat.model.poi.PoiMemberData;
import com.amt.wechat.model.poi.PoiUserData;

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
     * 店铺老枪邀请店员认领店铺
     * @param userData
     * @param name
     * @param mobile
     * @return
     */
    public BizPacket bossInviteIn(PoiUserData userData,String name,String mobile);


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

    /**
     * 设置是否自动续期
     * @param userData
     * @param feeRenew
     * @return
     */
    public BizPacket freeRenewSet(PoiUserData userData,int feeRenew);


    /**
     * 会员自动续期取消
     *
     * @param userData
     * @return
     */
    public BizPacket autoFeeRenewCencel(PoiUserData userData);


    /**
     * 余额密码设置
     * @param userData
     * @param pwd
     * @return
     */
    public BizPacket balancePwdSet(PoiUserData userData,String pwd);


    /**
     * 余额密码重置
     * @param userData
     * @param oldPwd
     * @param newPwd
     * @return
     */
    public BizPacket balancePwdReset(PoiUserData userData,String oldPwd,String newPwd);


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
}