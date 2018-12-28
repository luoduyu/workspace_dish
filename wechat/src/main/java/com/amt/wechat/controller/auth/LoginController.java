package com.amt.wechat.controller.auth;

import com.amt.wechat.domain.PhoneData;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.service.poi.IPOIUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private @Resource IPOIUserService poiUserService;

    /**
     * @param code 授权码
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv 加密算法的初始向量
     * @return
     */
    @RequestMapping(value = "/wechat/login",method = {RequestMethod.POST,RequestMethod.GET},produces = {"application/json","text/html"})
    public BizPacket weichatLogin(HttpServletRequest request,String code, String  encryptedData, String iv){
        if(code == null || code.trim().length() ==0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"参数code不正确!");
        }
        if(encryptedData == null || encryptedData.trim().length() == 0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"参数encryptedData不正确!");
        }
        if(iv == null || iv.trim().length() == 0){
           return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"参数iv不正确!");
        }
        PhoneData pd = extract(request.getSession());
        if(pd == null){
            return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(),"请先授权手机号访问权限,谢谢!");
        }

        logger.info("code={},encryptedData={},iv={},phone={}",code,encryptedData,iv,pd);

        return poiUserService.weichatLogin(code,encryptedData,iv,pd);
    }

    private PhoneData extract(HttpSession session){
        Object objPurePhoneNumber = session.getAttribute(PhoneData.SESSION_PHONE);
        Object objCountryCode = session.getAttribute(PhoneData.SESSION_PHONE_CC);
        if(objPurePhoneNumber == null){
            return null;
        }
        if(objCountryCode == null){
            return null;
        }
        return new PhoneData(objPurePhoneNumber.toString(),objCountryCode.toString());
    }


    /**
     * 获取手机号
     * @param code 授权码
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv 加密算法的初始向量
     * @return
     */
    @RequestMapping(value = "/wechat/login/phone",method = {RequestMethod.POST,RequestMethod.GET},produces = {"application/json","text/html"})
    public BizPacket weichatLogin4Phone(HttpServletRequest request,String code, String  encryptedData, String iv){
        if(code == null || code.trim().length() ==0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"参数code不正确!");
        }
        if(encryptedData == null || encryptedData.trim().length() == 0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"参数encryptedData不正确!");
        }
        if(iv == null || iv.trim().length() == 0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"参数iv不正确!");
        }

        logger.info("code={},encryptedData={},iv={}",code,encryptedData,iv);

        return poiUserService.weichatLogin4Phone(request.getSession(),code,encryptedData,iv);
    }


    @RequestMapping(value = "/wechat/test",method = {RequestMethod.POST,RequestMethod.GET},produces = {"application/json","text/html"})
    public BizPacket test(String code,String  encryptedData,String iv){
        if(code != null && code.trim().length() != 0) {
            logger.info("code={},encryptedData={},iv={}",code,encryptedData,iv);
            return BizPacket.success("参数有值!code=" + code+",encryptedData="+encryptedData);
        }
        return BizPacket.error_param_null("code参数为空!");
    }
}