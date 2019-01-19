package com.amt.wechat.service.redis;


import com.amt.wechat.model.poi.PoiUserData;

import java.io.IOException;

/**
 * Copyright (c) 2018 by CANSHU
 *
 *  redis 的访问类
 *
 * @author adu Create on 2018-12-18 19:10
 * @version 1.0
 */
public interface RedisService {


    public void addPoiUser(PoiUserData poiUserData) throws IOException;
    public PoiUserData getPoiUser(String accessToken);
    public PoiUserData getPoiUserById(String poiUserId);

    /**
     * 删除用户时
     * @param poiUserId
     */
    public void onUserRemoved(String poiUserId);

    /**
     * 当前60秒内是否能够发送短信
     *
     * @param mobile
     * @return true:可以;false:不可以
     */
    public boolean canSMSSendMinute(String mobile);

    /**
     * 当前这一天内是否还能够发送短信
     * @param mobile
     * @return 返回今日已发送次数
     */
    public long getSMSSendAmountToday(String mobile);

    /**
     * 短信发送成功后事件
     * @param mobile
     * @param smscode
     * @param timeoutMinutes 以分钟计的验证码存储时长
     */
    public void onSMSSend(String mobile, String smscode,int timeoutMinutes);

    /**
     * 获得此前存储的验证码
     * @param mobile
     * @return
     */
    public String getSMSCode(String mobile);
}