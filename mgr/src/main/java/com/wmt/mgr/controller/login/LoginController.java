package com.wmt.mgr.controller.login;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.commons.util.WmtUtil;
import com.wmt.mgr.common.Constants;
import com.wmt.mgr.dao.mgr.user.MgrUserDao;
import com.wmt.mgr.form.mgr.user.MgrUserForm;
import com.wmt.mgr.model.mgr.user.MgrUserData;
import com.wmt.mgr.service.mgr.user.UserService;
import com.wmt.mgr.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-22 11:44
 * @version 1.0
 */
@RestController
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    private @Resource RedisService redisService;
    private @Resource MgrUserDao mgrUserDao;
    private @Resource UserService userService;


    /**
     * @param smsCode
     * @param mobile
     *
     * @return
     */
    @RequestMapping(value = "/mgr/login/mobile",method = {RequestMethod.POST,RequestMethod.GET})
    public BizPacket mgrLoginByMobile(String smsCode, String  mobile,String authCode,HttpSession session){
        logger.info("smsCode={},mobile={},authCode={},_authCode={},sessionId={}",smsCode,mobile,authCode,session.getAttribute(Constants.MGR_IMG_CODE),session.getId());
        if(smsCode == null || smsCode.trim().length() ==0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"参数code不正确!");
        }
        if(mobile == null || mobile.trim().length() == 0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"参数mobile不正确!");
        }
        if(authCode == null || authCode.trim().length() == 0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"图形验证码不正确!");
        }
        Object objCode=session.getAttribute(Constants.MGR_IMG_CODE);
        if(objCode == null || objCode.toString().trim().length()==0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"图形验证码已失效!");
        }
        if(!objCode.toString().equals(authCode)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"图形验证码不正确!");
        }

        MgrUserData user = mgrUserDao.getMgrUserDataByMobile(mobile.trim());
        if(user == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"用户不存在!");
        }
        BizPacket packet = WmtUtil.check(user.getIsAccountNonLocked(),user.getIsEnabled(),user.getIsAccountNonExpired(),user.getIsCredentialsNonExpired());
        if(packet.getCode() != HttpStatus.OK.value()){
            return packet;
        }


        String code = redisService.getSMSCode(user.getMobile());
        if (code == null || code.trim().length() == 0 || !code.equalsIgnoreCase(smsCode)) {
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "手机验证码不对!");
        }

        logger.info("smsCode={},mobile={}",code,mobile);

        return userService.loginByMobile(user);
    }

    /**
     * accessToken登录
     *
     * @param accessToken
     * @return
     */
    @RequestMapping(value = "/mgr/login/token",method = {RequestMethod.POST,RequestMethod.GET},produces = {"application/json","text/html"})
    public BizPacket accessTokenLogin(String accessToken){
        if (StringUtils.isEmpty(accessToken)) {
            return BizPacket.error(HttpStatus.UNAUTHORIZED.value(),"access_token is empty!");
        }

        try {
            MgrUserData user = redisService.getMgrUser(accessToken);
            if (user == null) {
                return BizPacket.error(HttpStatus.UNAUTHORIZED.value(), "user not found or frozen!");
            }

            BizPacket packet = WmtUtil.check(user.getIsAccountNonLocked(),user.getIsEnabled(),user.getIsAccountNonExpired(),user.getIsCredentialsNonExpired());
            if(packet.getCode() != HttpStatus.OK.value()){
                return packet;
            }
            userService.onLoginByAccessToken(user);
            MgrUserForm form = MgrUserForm.buildResponse(user);
            return BizPacket.success(form);

        }catch (Exception ex){
            logger.error(ex.getMessage(),ex);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage());
        }
    }


    /**
     * @param pwd
     * @param mobile
     *
     * @return
     */
    @RequestMapping(value = "/mgr/login/pwd",method = {RequestMethod.POST,RequestMethod.GET})
    public BizPacket mgrLoginByPwd(String  mobile, String pwd, String authCode, HttpSession session){
        logger.info("pwd={},mobile={},authCode={},_authCode={},sessionId={}",pwd,mobile,authCode,session.getAttribute(Constants.MGR_IMG_CODE),session.getId());
        if(pwd == null || pwd.trim().length() ==0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"密码缺失!");
        }
        if(mobile == null || mobile.trim().length() == 0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"手机号必填!");
        }
        if(authCode == null || authCode.trim().length() == 0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"图形验证码不正确!");
        }
        Object objCode=session.getAttribute(Constants.MGR_IMG_CODE);
        if(objCode == null || objCode.toString().trim().length()==0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"图形验证码已失效!");
        }
        if(!objCode.toString().equals(authCode)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"图形验证码不正确!");
        }


        MgrUserData user = mgrUserDao.getMgrUserDataByMobile(mobile.trim());
        if(user == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"用户不存在!");
        }
        BizPacket packet = WmtUtil.check(user.getIsAccountNonLocked(),user.getIsEnabled(),user.getIsAccountNonExpired(),user.getIsCredentialsNonExpired());
        if(packet.getCode() != HttpStatus.OK.value()){
            return packet;
        }
        if(!user.getPassword().equals(pwd.trim())){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"密码或帐号错误!");
        }

        logger.info("mobile={},mobile={},authCode={}",mobile,pwd,authCode);

        return userService.loginByMobile(user);
    }

    @RequestMapping(value = "/mgr/token/fetch",method = {RequestMethod.POST,RequestMethod.GET},produces = {"application/json","text/html"})
    public BizPacket accessTokenFetch(String mobile) {

        if(mobile == null || mobile.trim().length() == 0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"参数mobile不正确!");
        }

        MgrUserData user = mgrUserDao.getMgrUserDataByMobile(mobile.trim());
        if(user == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"用户不存在!");
        }
        if(redisService.getMgrUser(user.getAccessToken()) == null){
            user.setAccessToken(null);
        }
        return BizPacket.success(user);
    }
}
