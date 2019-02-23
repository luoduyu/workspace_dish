package com.wmt.mgr.controller.base;

import com.wmt.mgr.common.Constants;
import com.wmt.mgr.model.mgr.user.MgrUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright (c) 2019 by CANSHU
 * 管理后台
 *
 * @author adu Create on 2019-02-18 17:52
 * @version 1.0
 */
@Component("mgrBaseController")
public class BaseController {

    private @Autowired HttpServletRequest request;

    protected MgrUserData getUser(){
        MgrUserData user = (MgrUserData)request.getAttribute(Constants.MGR_LOGGED_USER);
        return user;
    }
}