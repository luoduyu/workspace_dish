package com.amt.wechat.dao;


/**
 * Copyright (c) 2018 by CANSHU
 *
 *  redis 的访问类
 *
 * @author adu Create on 2018-12-18 19:10
 * @version 1.0
 */
public interface RedisDao {


    public void addAccessToken(String accessToken, String sessionKey,String openid);
    public String getAccessToken(String accessToken);

    public void set(String key, String value, long accessTokenValidMinutes);

    public void delete(String key);

}
