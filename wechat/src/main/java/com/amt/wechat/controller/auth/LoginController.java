package com.amt.wechat.controller.auth;

import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.service.poi.IPOIUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private @Resource IPOIUserService poiUserService;

    /**
     * @param code
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv 加密算法的初始向量
     * @return
     */
    @RequestMapping(value = "/wechat/login",method = {RequestMethod.POST,RequestMethod.GET},produces = {"application/json","text/html"})
    public BizPacket weichatLogin(String code, String  encryptedData, String iv){
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

        return poiUserService.weichatLogin(code,encryptedData,iv);
    }


    @RequestMapping(value = "/wx/test",method = {RequestMethod.POST,RequestMethod.GET},produces = {"application/json","text/html"})
    public BizPacket test(String code,String  encryptedData,String iv){
        if(code != null && code.trim().length() != 0) {
            logger.info("code={},encryptedData={},iv={}",code,encryptedData,iv);
            return BizPacket.success("参数有值!code=" + code+",encryptedData="+encryptedData);
        }
        return BizPacket.error_param_null("code参数为空!");
    }
}