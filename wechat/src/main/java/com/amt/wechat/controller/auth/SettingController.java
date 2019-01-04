package com.amt.wechat.controller.auth;

import com.amt.wechat.controller.base.BaseController;
import com.amt.wechat.dao.poi.PoiDao;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.sms.AliSMS;
import com.amt.wechat.domain.util.WechatUtil;
import com.amt.wechat.form.AllBasicSettingForm;
import com.amt.wechat.form.BasicSettingForm;
import com.amt.wechat.model.poi.PoiData;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.service.login.LoginService;
import com.amt.wechat.service.poi.IPoiUserService;
import com.amt.wechat.service.poi.PoiService;
import com.amt.wechat.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-04 14:05
 * @version 1.0
 */
@RestController
public class SettingController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(SettingController.class);

    private @Resource IPoiUserService poiUserService;
    private @Resource PoiService poiService;
    private @Resource RedisService redisService;
    private @Resource LoginService loginService;
    private @Resource PoiDao poiDao;


    /**
     * (商户老板)手机号授权
     * @param name
     * @param mobile
     * @param smsCode
     * @return
     */
    @RequestMapping(value = "/setting/auth/boss/mobile",method = {RequestMethod.GET,RequestMethod.POST},produces = {"application/json","text/html"})
    public BizPacket wechatAuthByMobile(String name, String mobile, String smsCode){
        String code = redisService.getSMSCode(mobile);
        if(code == null || code.trim().length() ==0 || !code.equalsIgnoreCase(smsCode)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"手机验证码不对!");
        }
        if(StringUtils.isEmpty(name)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"抱歉，请填写姓名!");
        }


        PoiUserData userData = getUser();
        if(!StringUtils.isEmpty(userData.getMobile()) && !StringUtils.isEmpty(userData.getName())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"姓名和手机号已经授权认证过了!");
        }
        if(StringUtils.isEmpty(userData.getMobile())){
            if(StringUtils.isEmpty(mobile)){
                return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"请填写手机号!");
            }
        }else{
            if(!userData.getMobile().equalsIgnoreCase(mobile)){
                return BizPacket.error(HttpStatus.CONFLICT.value(),"当前手机号与已有手机号不一致!原手机号:"+userData.getMobile());
            }
        }

        return poiUserService.auth4Mobile(name,mobile,getUser());
    }



    /**
     * (商户老板)手机号授权验证码发送
     * @param mobile 用户手机号
     * @param type 1:短信, 2:语音; 默认为短信类型
     * @return
     */
    @RequestMapping(value="/setting/sms/auth",method = RequestMethod.POST)
    public BizPacket sendSMS4Auth_mobile(String mobile, Integer type) {
        if(type == null){
            type = 1;
        }
        return sendSMS(mobile,type,30, AliSMS.TEMP_CODE_0531);
    }


    /**
     * 验证码发送-运营和开店申请共用
     * @param mobile 用户手机号
     * @param type 1:短信, 2:语音; 默认为短信类型
     * @return
     */
    @RequestMapping(value="/setting/sms/go",method = RequestMethod.POST)
    public BizPacket sendSMS4GO(String mobile, Integer type) {
        if(type == null){
            type = 1;
        }
        return sendSMS(mobile,type,10,AliSMS.TEMP_CODE_0531);
    }

    /**
     *
     * @param timeoutMinutes 验证码的有效期(存储时长,单位:分钟)
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


    /**
     * 验证码发送-设置-手机号更换
     *
     * @param mobile 用户手机号
     * @param type 1:短信, 2:语音; 默认为短信类型
     * @return
     */
    @RequestMapping(value="/setting/sms/replace",method = RequestMethod.POST)
    public BizPacket sendSMS4Replace(String mobile, Integer type) {
        if(type == null){
            type = 1;
        }
        return sendSMS(mobile,type,1,AliSMS.TEMP_CODE_0531);
    }

    @RequestMapping(value="/setting/mobile/replace",method = RequestMethod.POST)
    public BizPacket mobileReplace(String oldSMSCode,String newMobile, String newSMSCode) {
        if(StringUtils.isEmpty(oldSMSCode) || StringUtils.isEmpty(newMobile) || StringUtils.isEmpty(newSMSCode)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"缺少参数");
        }
        if (!WechatUtil.isMobileNO(newMobile)) {
            return BizPacket.error(HttpStatus.PAYMENT_REQUIRED.value(), "新手机号格式不正确!");
        }

        String code = redisService.getSMSCode(newMobile);
        if(code == null || code.trim().length() ==0 || !code.equalsIgnoreCase(newSMSCode)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"手机验证码不对!");
        }

        return poiUserService.mobileReplace(getUser(),newMobile);
    }

    @GetMapping(value="/setting/poi/basic/get")
    public BizPacket memberBasicSettingGet(){
        try {
            AllBasicSettingForm form = new AllBasicSettingForm();
            PoiUserData userData = getUser();
            form.setMemberName(userData.getName());
            form.setMemberMobile(userData.getMobile());

            PoiData poiData =  poiDao.getPoiData(userData.getPoiId());
            form.setPoiCateId(poiData.getCateId());
            form.setPoiBrandName(poiData.getBrandName());
            form.setPoiCountry(poiData.getCountry());
            form.setPoiProvince(poiData.getProvince());
            form.setPoiCity(poiData.getCity());
            form.setPoiDistricts(poiData.getDistricts());
            form.setPoiStreet(poiData.getStreet());
            form.setPoiAddress(poiData.getAddress());

            return BizPacket.success(form);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @RequestMapping(value = "/setting/poi/basic/set",method ={RequestMethod.POST,RequestMethod.GET},produces = {"application/json","text/html"})
    public BizPacket memberBasicSetup(BasicSettingForm basicSettingForm){

        if(StringUtils.isEmpty(basicSettingForm.getPoiBrandName())){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"品牌名称不能为空!");
        }

        if(StringUtils.isEmpty(basicSettingForm.getMemberName())){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"姓名不能为空!");
        }

        PoiUserData userData = getUser();
        BizPacket  packet = poiUserService.updatePoiUserName(userData.getId(),basicSettingForm.getMemberName());
        if(packet.getCode() != HttpStatus.OK.value()){
            return packet;
        }
        return poiService.updatePoi(userData,basicSettingForm);
    }
}