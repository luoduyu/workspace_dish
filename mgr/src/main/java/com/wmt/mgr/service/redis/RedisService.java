package com.wmt.mgr.service.redis;

import com.wmt.mgr.model.user.MgrUserData;

import java.io.IOException;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-20 16:14
 * @version 1.0
 */
public interface RedisService {

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


    public void addMgrUser(MgrUserData mgrUserData, String oldAccessToken) throws IOException;
    public MgrUserData getMgrUser(String accessToken);
    public MgrUserData getMgrUserById(String mgrUserId);

    /**
     * 删除用户时
     * @param mgrUserId
     */
    public void onUserRemoved(String mgrUserId);
}
