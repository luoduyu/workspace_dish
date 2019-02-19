package com.amt.wechat.domain.config;

import com.alibaba.fastjson.JSON;
import com.amt.wechat.common.constants.Constants;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.DateTimeUtil;
import com.amt.wechat.domain.util.WechatUtil;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-29 10:32
 * @version 1.0
 */
@Component("wechatHandlerInterceptor")
public class WechatHandlerInterceptor implements HandlerInterceptor {
    private static final Logger traceLog = LoggerFactory.getLogger("wechatTraceLog");
    private static final Logger logger = LoggerFactory.getLogger(WechatHandlerInterceptor.class);

    private  @Autowired RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getParameter(Constants.REQ_PARAM_ACCESSTOKEN);
        if (StringUtils.isEmpty(accessToken)) {
            traceLog(request, accessToken,null);
            return handlerError(response,HttpStatus.UNAUTHORIZED.value(), "access_token is empty!");
        }

        try {
            PoiUserData user = redisService.getPoiUser(accessToken);
            if (user == null) {
                traceLog(request, accessToken,null);
                return handlerError(response,HttpStatus.UNAUTHORIZED.value(),"user not found or frozen!");
            }

            BizPacket result = WechatUtil.check(user);
            if(result.getCode() != HttpStatus.OK.value()){
                return handlerError(response,result.getCode(), result.getMsg());
            }

            request.setAttribute(Constants.WECHAT_LOGGED_USER, user);
            traceLog(request, accessToken, user);
            return true;
        } catch (Exception e) {
            return handlerError(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }


    private static boolean handlerError(HttpServletResponse response,int statusCode, String msg) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        BizPacket bizPacket =  BizPacket.error(statusCode,msg);
        String s = JSON.toJSONString(bizPacket);
        response.getWriter().write(s);
        return false;
    }

    private void traceLog(HttpServletRequest request, String accessToken,PoiUserData userData) {
        try {
            String addr = request.getRemoteAddr();
            String uri = request.getRequestURI();
            String params = asParams2String(request);
            String token = accessToken == null ? "" : accessToken.trim();
            traceLog.info("createDate={},ip={},reqURI={},accessToken={},user={},params={}", DateTimeUtil.now(), addr,uri, token,userData, params);
        } catch (Exception e) {
            logger.error("userData="+userData+",accessToken=" + accessToken + ",e=" + e.getMessage(), e);
        }
    }

    private String asParams2String(HttpServletRequest request) {
        StringBuilder params = new StringBuilder();
        Enumeration<String> pNames = request.getParameterNames();
        while (pNames.hasMoreElements()) {
            String name = pNames.nextElement();
            String value = request.getParameter(name);
            params.append(name).append(":").append(value).append(",");
        }
        if (params.length() >= 2) {
            params.deleteCharAt(params.length() - 1);
        }

        return params.toString();
    }
}