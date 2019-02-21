package com.wmt.wechat.model.balance;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Date;

/**
 * Copyright (c) 2019 by CANSHU
 *  余额相关设置
 *
 * @author adu Create on 2019-02-01 11:48
 * @version 1.0
 */
public class BalanceSettingData implements Serializable {
    private static final long serialVersionUID = 4711960600826227720L;


    /**
     * id
     */
    private Integer id;

    /**
     * 方案名称
     */
    private String name;

    /**
     * 0:不生效,1:生效;是否当前生效,所有方案中始终只能有一条记录值为1
     */
    private Integer isEnabled;

    /**
     * 余额充值时红包奖励的下限,单位:分
     */
    private Integer redRewardLowerLimit;

    /**
     * 红包奖励的百分比,基数10000,值为100表示10%
     */
    private Integer redRewardPercent;

    /**
     * 日期
     */
    private Date  updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Integer getRedRewardLowerLimit() {
        return redRewardLowerLimit;
    }

    public void setRedRewardLowerLimit(Integer redRewardLowerLimit) {
        this.redRewardLowerLimit = redRewardLowerLimit;
    }

    public Integer getRedRewardPercent() {
        return redRewardPercent;
    }

    public void setRedRewardPercent(Integer redRewardPercent) {
        this.redRewardPercent = redRewardPercent;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
       return JSON.toJSONString(this);
    }
}