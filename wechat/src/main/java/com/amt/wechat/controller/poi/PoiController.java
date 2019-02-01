package com.amt.wechat.controller.poi;

import com.amt.wechat.controller.base.BaseController;
import com.amt.wechat.dao.poi.PoiAccountDao;
import com.amt.wechat.dao.poi.PoiDao;
import com.amt.wechat.dao.poi.PoiUserDao;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.sms.AliSMS;
import com.amt.wechat.domain.util.WechatUtil;
import com.amt.wechat.form.basic.BasicSettingForm;
import com.amt.wechat.form.poi.PoiForm;
import com.amt.wechat.model.poi.PoiAccountData;
import com.amt.wechat.model.poi.PoiCandidate;
import com.amt.wechat.model.poi.PoiData;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.service.login.LoginService;
import com.amt.wechat.service.poi.EmplIdentity;
import com.amt.wechat.service.poi.PoiService;
import com.amt.wechat.service.poi.PoiUserService;
import com.amt.wechat.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  店铺类controller
 *
 * @author adu Create on 2019-01-03 20:24
 * @version 1.0
 */
@RestController
public class PoiController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(PoiController.class);
    private @Resource PoiUserService poiUserService;
    private @Resource PoiService poiService;
    private @Resource RedisService redisService;
    private @Resource LoginService loginService;
    private @Resource PoiDao poiDao;
    private @Resource PoiUserDao poiUserDao;
    private @Resource PoiAccountDao poiAccountDao;


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

        if(userData.getIsMaster() != EmplIdentity.NONE.value()){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"非法调用!");
        }

        if(!StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"已经授权认证过店铺了!");
        }

        /*
        if(!StringUtils.isEmpty(userData.getMobile()) && !StringUtils.isEmpty(userData.getName())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"姓名和手机号已经授权认证过了!");
        }
        */

        /*
        if(!StringUtils.isEmpty(userData.getMobile())){
            if(!userData.getMobile().equalsIgnoreCase(mobile)){
                return BizPacket.error(HttpStatus.CONFLICT.value(),"当前手机号与原有手机号不一致!原手机号:"+userData.getMobile());
            }
        }
        */

        return poiUserService.auth4Boss(name,mobile,userData);
    }


    /**
     * 店铺授权-店员姓名和手机号申报
     * @param name
     * @param mobile
     * @param smsCode
     * @return
     */
    @RequestMapping(value = "/setting/employee/upd",method = {RequestMethod.GET,RequestMethod.POST},produces = {"application/json","text/html"})
    public BizPacket employeeData(String name, String mobile, String smsCode){
        if(StringUtils.isEmpty(mobile)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"手机号不能为空!");
        }
        String code = redisService.getSMSCode(mobile);
        if(code == null || code.trim().length() ==0 || !code.equalsIgnoreCase(smsCode)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"手机验证码不对!");
        }

        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(name)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"抱歉,姓名不能为空!");
        }
        logger.info("{}申请更新姓名(name={})和个人手机号(mobile={})!",userData,mobile);
        return poiUserService.employeeSet(name.trim(),mobile.trim(),userData);
    }

    @PostMapping(value="/setting/poi/bind")
    public BizPacket poiBind(@RequestParam("inviteDataId") Integer inviteDataId){
        PoiUserData userData = getUser();
        if(!StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你当前有店铺了!");
        }
        PoiCandidate candidate = poiUserDao.getPoiCandidateById(inviteDataId);
        if(candidate == null){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"邀请已经失效!");
        }
        return poiUserService.poiBind(userData,candidate);
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

        return poiUserService.bossInviteIn(userData,name,mobile);
    }


    /**
     * 店员受邀请集合
     * @return
     */
    @PostMapping(value="/setting/poi/invite/list")
    public BizPacket inviteInList(){
        PoiUserData userData = getUser();
        if(!StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你已经有店铺了!");
        }

        if(StringUtils.isEmpty(userData.getMobile())){
            return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(),"你必须先申请手机号!");
        }

        List<PoiCandidate> list = poiUserDao.getPoiCandidateList(userData.getMobile());
        return BizPacket.success(list);
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
            String masterMobile = poiUserDao.getMasterMobile(poiData.getId(),EmplIdentity.MASTER.value());
            PoiAccountData accountData =  poiAccountDao.getAccountData(userData.getPoiId());

            PoiForm basicData = PoiData.createFrom(poiData,accountData,masterMobile);
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
        if(!StringUtils.isEmpty(basicSettingForm.getMemberName())){
            if(!userData.getName().equalsIgnoreCase(basicSettingForm.getMemberName().trim())){
                BizPacket  packet = poiUserService.updatePoiUserName(userData,basicSettingForm.getMemberName());
                if(packet.getCode() != HttpStatus.OK.value()){
                    return packet;
                }
            }
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

    /**
     * 余额支付密码设置
     * @return
     */
    @PostMapping(value = "/setting/poi/balance/pwd/required")
    public BizPacket isRequiredBalancePwdSet(Integer flag){
        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        if(StringUtils.isEmpty(flag)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"参数不能为空!");
        }

        if(flag != 0 && flag != 1){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"参数值非法:"+flag);
        }
        return poiService.balancePwdRequired(userData,flag);
    }

    /**
     * 密码忘记--验证码校验
     * @param smsCode
     * @return
     */
    @PostMapping(value = "/setting/poi/balance/forget/sms")
    public BizPacket forget_sms_verify(String smsCode){
        try {
            PoiUserData userData = getUser();
            if(StringUtils.isEmpty(userData.getPoiId())){
                return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
            }

            if(StringUtils.isEmpty(userData.getMobile())){
                return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有设置手机号!");
            }

            // 手机及验证码校验
            String _smsCode = redisService.getSMSCode(userData.getMobile());
            if(smsCode == null || smsCode.trim().length() ==0 || !smsCode.equalsIgnoreCase(_smsCode)){
                redisService.onSmsVerify(false,userData.getId(),userData.getMobile());
                return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"手机验证码不对!");
            }

            redisService.onSmsVerify(true,userData.getId(),userData.getMobile());
            return BizPacket.success();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    /**
     * 密码忘记--密码设置
     * @param newPwd
     * @return
     */
    @PostMapping(value = "/setting/poi/balance/forget/set")
    public BizPacket forget_pwd_set(String newPwd){
        PoiUserData userData = getUser();
        try {
            if(StringUtils.isEmpty(newPwd)){
                return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"密码不能为空!");
            }

            if(newPwd.trim().length() >50){
                return BizPacket.error(HttpStatus.URI_TOO_LONG.value(),"密码过长(最长50个字符)!");
            }

            if(StringUtils.isEmpty(userData.getPoiId())){
                return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
            }

            if(StringUtils.isEmpty(userData.getMobile())){
                return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有设置手机号!");
            }

            String _mobile = redisService.getMobile4Forget(userData.getId());
            if(!userData.getMobile().equalsIgnoreCase(_mobile)){
                return BizPacket.error(HttpStatus.FORBIDDEN.value(),"请使用当前手机!");
            }

            return poiService.forgetReset(userData,newPwd);
        } catch (Exception e) {
            logger.error("userData="+userData+",newPwd="+newPwd+",e"+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
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
        if(userData.getId().equalsIgnoreCase(userId)){
            return  BizPacket.error(HttpStatus.FORBIDDEN.value(), "参数非法!");
        }
        return poiUserService.bossTransferTo(userData,userId);
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

        PoiUserData boss = getUser();
        if(StringUtils.isEmpty(boss.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }
        if(boss.getIsMaster() != EmplIdentity.MASTER.value()){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(), "你不是店主!");
        }

        if(boss.getId().equalsIgnoreCase(userId)){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(), "不能删除自己!");
        }
        return poiUserService.employeeRM(boss,userId);
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

    @GetMapping(value = "/setting/getwxacodeunlimit")
    public BizPacket getwxacodeunlimit(){
        PoiUserData userData  = getUser();
        return poiService.getwxacodeunlimit(userData);
    }
}