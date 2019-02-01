package com.amt.wechat.service.redis;


import com.amt.wechat.model.balance.BalanceSettingData;
import com.amt.wechat.model.poi.PoiUserData;

import java.io.IOException;
import java.util.Map;

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


    /**
     *
     * @param verifyResult true:表示校验成功,false:表示校验失败;
     * @param userId
     * @param mobile
     */
    public void onSmsVerify(boolean verifyResult,String userId,String mobile);

    /**
     * 获得暂存的手机号
     * @param userId
     * @return
     */
    public String getMobile4Forget(String userId);


    public void onSnapSucc(int cateId,String timeFrameStart);

    public int getSnapNum(int cateId,String timeFrameStart);

    /**
     * 当天某分类下的销量统计
     * @param cateId
     * @return k:hhMMss
     */
    public Map<String,Integer> countTodaySnapSoldNum(int cateId);


    /**
     * 余额设置有改变时收到此消息
      * @param settingJSONMessage
     */
    public void onBalanceSettingChanged(String settingJSONMessage);
    public BalanceSettingData getBalanceSetting();


    /**
     * 获取微信小程序全局唯一后台接口调用凭据(access_token)
     * 调调用绝大多数后台接口时都需使用 access_token，开发者需要进行妥善保存
     * @return
     */
    public String getWeixinAccessToken();
}