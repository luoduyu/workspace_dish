package com.amt.wechat.controller.auth;

import com.amt.wechat.domain.PhoneData;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.sms.AliSMS;
import com.amt.wechat.domain.util.WechatUtil;
import com.amt.wechat.service.login.LoginService;
import com.amt.wechat.service.poi.IPOIUserService;
import com.amt.wechat.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private @Resource IPOIUserService poiUserService;
    private @Resource RedisService redisService;
    private @Resource LoginService loginService;

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

        /*
        PhoneData pd = extract(request.getSession());
        if(pd == null){
            return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(),"请先授权手机号访问权限,谢谢!");
        }
        */

        logger.info("code={},encryptedData={},iv={}",code,encryptedData,iv);

        return poiUserService.weichatLogin(code,encryptedData,iv,null);
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
    @RequestMapping(value = "/wechat/phone/fetch",method = {RequestMethod.POST,RequestMethod.GET},produces = {"application/json","text/html"})
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

    @RequestMapping(value = "/wechat/test/login")
    public BizPacket testLogin(){
        try {
            return poiUserService.testLogin();
        } catch (IOException e) {
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    /**
     * 手机号授权验证码发送
     * @param mobile 用户手机号
     * @param type 1:短信, 2:语音; 默认为短信类型
     * @return
     */
    @RequestMapping(value="/wechat/sms/auth/mobile",method = RequestMethod.GET)
    public BizPacket sendSMS4Auth_mobile(String mobile, Integer type) {
        if(type == null){
            type = 1;
        }
        return sendSMS(mobile,type,30,AliSMS.TEMP_CODE_0531);
    }


    /**
     * 运营申请手机验证码发送
     * @param mobile 用户手机号
     * @param type 1:短信, 2:语音; 默认为短信类型
     * @return
     */
    @RequestMapping(value="/wechat/sms/go",method = RequestMethod.GET)
    public BizPacket sendSMS4GO(String mobile, Integer type) {
        if(type == null){
            type = 1;
        }
        return sendSMS(mobile,type,10,AliSMS.TEMP_CODE_0531);
    }

    /**
     *
     * @param timeoutMinutes 验证码的有效期(存储时长)
     * @return
     */
    private BizPacket sendSMS(String mobile,int type,int timeoutMinutes,String templateCode) {
        if(StringUtils.isEmpty((mobile))){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"请输入手机号");
        }

        if (!WechatUtil.isMobileNO(mobile)) {
            return BizPacket.error(HttpStatus.PAYMENT_REQUIRED.value(), "手机号格式不正确!");
        }

        try {
            // 60秒锁验证
            boolean lock = redisService.canSMSSendMinute(mobile);
            if (!lock) {
                return BizPacket.error(HttpStatus.PAYMENT_REQUIRED.value(), "请稍等，1分钟只能发送一次验证码，谢谢!");
            }

            // 每日15次上限验证
            long count = redisService.getSMSSendAmountToday(mobile);
            if (count >= 16) {
                return BizPacket.error(HttpStatus.PAYMENT_REQUIRED.value(), "您好，每个手机号每天只能登录15次，谢谢!");
            }

            BizPacket packet= loginService.sendSMS(mobile,timeoutMinutes, templateCode);
            return packet;
        } catch (Exception e) {
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),"服务器忙,请稍后重试!");
        }
    }
}