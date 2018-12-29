package com.amt.wechat.dao;


import com.amt.wechat.model.poi.POIUserData;

import java.io.IOException;

/**
 * Copyright (c) 2018 by CANSHU
 *
 *  redis 的访问类
 *
 * @author adu Create on 2018-12-18 19:10
 * @version 1.0
 */
public interface RedisDao {


    public void addPOIUser(POIUserData poiUserData) throws IOException;
    public POIUserData getPOIUser(String accessToken);


    public void delete(String key);

}
