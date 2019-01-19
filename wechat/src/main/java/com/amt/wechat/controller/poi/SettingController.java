package com.amt.wechat.controller.poi;

import com.amt.wechat.controller.base.BaseController;
import com.amt.wechat.dao.poi.PoiDao;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.sms.AliSMS;
import com.amt.wechat.domain.util.WechatUtil;
import com.amt.wechat.form.basic.BasicSettingForm;
import com.amt.wechat.model.poi.PoiBasicData;
import com.amt.wechat.model.poi.PoiData;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.service.login.LoginService;
import com.amt.wechat.service.poi.EmplIdentity;
import com.amt.wechat.service.poi.IPoiUserService;
import com.amt.wechat.service.poi.PoiService;
import com.amt.wechat.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  设置/管理类controller
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
     * 店铺授权-BOSS手机号申报
     * @param name
     * @param mobile
     * @param smsCode
     * @return
     */
    @RequestMapping(value = "/setting/auth/boss/mobile",method = {RequestMethod.GET,RequestMethod.POST},produces = {"application/json","text/html"})
    public BizPacket wechatAuth4Boss(String name, String mobile, String smsCode){
        if(StringUtils.isEmpty(mobile)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"手机号不能为空!");
        }
        String code = redisService.getSMSCode(mobile);
        if(code == null || code.trim().length() ==0 || !code.equalsIgnoreCase(smsCode)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"手机验证码不对!");
        }
        if(StringUtils.isEmpty(name)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"抱歉,姓名不能为空!");
        }

        PoiUserData userData = getUser();
        if(!StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"已经授权认证过店铺了!");
        }
        if(!StringUtils.isEmpty(userData.getMobile()) && !StringUtils.isEmpty(userData.getName())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"姓名和手机号已经授权认证过了!");
        }
        if(!StringUtils.isEmpty(userData.getMobile())){
            if(!userData.getMobile().equalsIgnoreCase(mobile)){
                return BizPacket.error(HttpStatus.CONFLICT.value(),"当前手机号与原有手机号不一致!原手机号:"+userData.getMobile());
            }
        }

        return poiUserService.auth4Boss(name,mobile,userData);
    }


    /**
     * 店铺授权-店员姓名和手机号申报
     * @param name
     * @param mobile
     * @param smsCode
     * @return
     */
    @RequestMapping(value = "/setting/auth/employee/mobile",method = {RequestMethod.GET,RequestMethod.POST},produces = {"application/json","text/html"})
    public BizPacket wechatAuth4Employee(String name, String mobile, String smsCode){
        if(StringUtils.isEmpty(mobile)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"手机号不能为空!");
        }
        String code = redisService.getSMSCode(mobile);
        if(code == null || code.trim().length() ==0 || !code.equalsIgnoreCase(smsCode)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"手机验证码不对!");
        }

        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getName())){
            if(StringUtils.isEmpty(name)){
                return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"抱歉,姓名不能为空!");
            }
        }

        if(!StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"已经授权认证过店铺了!");
        }

        /*
        允许仅仅认证了手机号且之前认证为老板的‘人’重新认证为店员
        if(!StringUtils.isEmpty(userData.getMobile()) && !StringUtils.isEmpty(userData.getName())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"姓名和手机号已经授权认证过了!");
        }
        */
        if(!StringUtils.isEmpty(userData.getMobile())){
            if(!userData.getMobile().equalsIgnoreCase(mobile)){
                return BizPacket.error(HttpStatus.CONFLICT.value(),"当前手机号与原有手机号不一致!原手机号:"+userData.getMobile());
            }
        }

        return poiUserService.auth4Empl(name,mobile,userData);
    }

    /**
     * 邀请店员注册及登录
     * @return
     */
    @PostMapping(value="/setting/poi/employee/invite")
    public BizPacket bossInviteIn(@RequestParam("name") String name,@RequestParam("mobile") String mobile){
        if(StringUtils.isEmpty(name) || StringUtils.isEmpty(mobile)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"用户和密码参数非法!");
        }
        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        if(userData.getIsMaster() != EmplIdentity.MASTER.value()){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(), "你不是店主!");
        }

        return poiService.bossInviteIn(userData,name,mobile);
    }


    /**
     * 验证码发送 -- 账户类操作
     * @param mobile 手机号
     * @param type 1:短信, 2:语音; 默认为短信类型
     * @return
     */
    @RequestMapping(value="/setting/sms/auth",method = RequestMethod.POST)
    public BizPacket sendSMS4Auth_mobile(String mobile, Integer type) {
        if(type == null){
            type = 1;
        }
        return sendSMS(mobile,type,5, AliSMS.SMS_TEMPLATE_ACCOUNT);
    }


    /**
     * 验证码发送 -- 运营操作类：
     * @param mobile 手机号
     * @param type 1:短信, 2:语音; 默认为短信类型
     * @return
     */
    @RequestMapping(value="/setting/sms/go",method = RequestMethod.POST)
    public BizPacket sendSMS4GO(String mobile, Integer type) {
        if(type == null){
            type = 1;
        }
        return sendSMS(mobile,type,30,AliSMS.SMS_TEMPLATE_OP);
    }

    /**
     *
     * @param type 1:短信, 2:语音; 默认为短信类型
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
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),"服务器忙,请稍后重试!");
        }
    }


    /**
     * BOSS手机号更换
     * @param oldSMSCode
     * @param newMobile
     * @param newSMSCode
     * @return
     */
    @RequestMapping(value="/setting/mobile/replace",method = RequestMethod.POST)
    public BizPacket mobileReplace(String oldSMSCode,String newMobile, String newSMSCode) {

        if (!WechatUtil.isMobileNO(newMobile)) {
            return BizPacket.error(HttpStatus.PAYMENT_REQUIRED.value(), "新手机号格式不正确!");
        }

        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getMobile())){
            return BizPacket.error(HttpStatus.UNAUTHORIZED.value(),"您还未申报过手机号!");
        }

        /*
        // 允许无店铺的BOSS更换手机号
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }
        */

        if(userData.getIsMaster() != EmplIdentity.MASTER.value()){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(), "你不是店主!");
        }

        // 原手机及验证码校验
        String _oldSMSCode = redisService.getSMSCode(userData.getMobile());
        if(oldSMSCode == null || oldSMSCode.trim().length() ==0 || !oldSMSCode.equalsIgnoreCase(_oldSMSCode)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"原手机验证码不对!");
        }


        // 新手机及验证码校验
        String _newSMSCode = redisService.getSMSCode(newMobile);
        if(newSMSCode == null || newSMSCode.trim().length() ==0 || !newSMSCode.equalsIgnoreCase(_newSMSCode)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"新手机验证码不对!");
        }

        return poiUserService.mobileReplace(userData,newMobile);
    }


    /**
     * 店铺信息获取
     * @return
     */
    @GetMapping(value="/setting/poi/basic/get")
    public BizPacket memberBasicSettingGet(){
        PoiUserData userData = getUser();
        /*
        允许店内成员均能获取店铺信息
        if(userData.getIsMaster() != EmplIdentity.MASTER.value()){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(), "你不是店主!");
        }
        */

        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        try {
            PoiData poiData =  poiDao.getPoiData(userData.getPoiId());
            PoiBasicData basicData = PoiData.createFrom(poiData);
            return BizPacket.success(basicData);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    /**
     * '会员信息设置'之提交
     * @param basicSettingForm
     * @return
     */
    @RequestMapping(value = "/setting/poi/basic/set",method ={RequestMethod.POST,RequestMethod.GET},produces = {"application/json","text/html"})
    public BizPacket memberBasicSetup(BasicSettingForm basicSettingForm){
        PoiUserData userData = getUser();

        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        if(userData.getIsMaster() != EmplIdentity.MASTER.value()){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(), "你不是店主!");
        }


        if(StringUtils.isEmpty(basicSettingForm.getPoiBrandName())){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"品牌名称不能为空!");
        }
        // 更新会员名称
        BizPacket  packet = poiUserService.updatePoiUserName(userData,basicSettingForm.getMemberName());
        if(packet.getCode() != HttpStatus.OK.value()){
            return packet;
        }

        // 更新店铺信息
        return poiService.updatePoi(userData,basicSettingForm);
    }


    /**
     * 店铺成员列表
     * @return
     */
    @PostMapping(value = "/setting/poi/employee/list")
    public BizPacket employeeList(){
        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        if(userData.getIsMaster() != EmplIdentity.MASTER.value()){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(), "你不是店主!");
        }

        return poiUserService.employeeList(userData);
    }


    /**
     * 余额支付密码设置
     * @return
     */
    @PostMapping(value = "/setting/poi/balance/pwd/set")
    public BizPacket balancePwdSet(String pwd){
        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        if(StringUtils.isEmpty(pwd)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"密码不能为空!");
        }
        if(pwd.trim().length() >50){
            return BizPacket.error(HttpStatus.URI_TOO_LONG.value(),"密码过长(最长50个字符)!");
        }
        return poiService.balancePwdSet(userData,pwd);
    }

    @PostMapping(value = "/setting/poi/balance/pwd/reset")
    public BizPacket balancePwdReset(String oldPwd,String newPwd){
        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        if(StringUtils.isEmpty(newPwd)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"密码不能为空!");
        }
        if(newPwd.trim().length() >50){
            return BizPacket.error(HttpStatus.URI_TOO_LONG.value(),"密码过长(最长50个字符)!");
        }

        if(StringUtils.isEmpty(oldPwd)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"原密码不能为空!");
        }
        return poiService.balancePwdReset(userData,oldPwd,newPwd);
    }

    @PostMapping(value = "/setting/poi/boss/transfer")
    public BizPacket bossTransferTo(@RequestParam("userId") String userId){
        if(StringUtils.isEmpty(userId)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"受让人参数不能为空!");
        }

        PoiUserData userData = getUser();

        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        if(userData.getIsMaster() != EmplIdentity.MASTER.value()){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(), "你不是店主!");
        }
        return poiService.bossTransferTo(userData,userId);
    }


    /**
     * 店员删除
     * @param userId
     * @return
     */
    @PostMapping(value = "/setting/poi/employee/rm")
    public BizPacket employeeRM(@RequestParam("userId") String userId){
        if(StringUtils.isEmpty(userId)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"受让人参数不能为空!");
        }

        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }
        if(userData.getIsMaster() != EmplIdentity.MASTER.value()){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(), "你不是店主!");
        }
        return poiService.employeeRM(userData,userId);
    }

    @PostMapping(value = "/setting/poi/auth/ele")
    public BizPacket eleAuth(String accountName,String accountPwd){
        PoiUserData userData  = getUser();
        return poiService.eleAuth(userData,accountName,accountPwd);
    }

    @PostMapping(value = "/setting/poi/auth/mt")
    public BizPacket mtAuth(String accountName,String accountPwd){
        PoiUserData userData  = getUser();
        return poiService.mtAuth(userData,accountName,accountPwd);
    }
}