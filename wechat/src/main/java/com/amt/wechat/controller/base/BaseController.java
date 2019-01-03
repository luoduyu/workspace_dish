package com.amt.wechat.controller.base;

import com.amt.wechat.common.Constants;
import com.amt.wechat.model.poi.PoiUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-29 10:18
 * @version 1.0
 */
@Component
public class BaseController {

    private @Autowired   HttpServletRequest request;

    protected PoiUserData getUser(){
        PoiUserData user = (PoiUserData)request.getAttribute(Constants.WECHAT_LOGGED_USER);
        return user;
    }
}