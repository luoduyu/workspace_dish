package com.amt.wechat.controller.go;

import com.amt.wechat.controller.base.BaseController;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.form.OperationalForm;
import com.amt.wechat.model.poi.POIUserData;
import com.amt.wechat.service.go.GoService;
import com.amt.wechat.service.poi.IPOIUserService;
import com.amt.wechat.service.redis.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Copyright (c) 2018 by CANSHU
 *
 *  运营类controller
 * @author adu Create on 2018-12-29 16:41
 * @version 1.0
 */
@RestController
public class GoController extends BaseController {

    private @Resource RedisService redisService;
    private @Resource IPOIUserService poiUserService;
    private @Resource GoService goService;

    /**
     * 运营开店申请提交
     * @param form
     * @return
     */
    @RequestMapping(value = "/go/form/submit",method = RequestMethod.POST,produces = {"application/json","text/html"})
    public BizPacket appSubmit(@RequestBody OperationalForm form){
        String code = redisService.getSMSCode(form.getContactMobile());
        if(code == null || code.trim().length() ==0 || !code.equalsIgnoreCase(form.getSmsCode())){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"手机验证码不对!");
        }

        if(StringUtils.isEmpty(form.getBrandName())){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"品牌名称为必填项!");
        }
        if(StringUtils.isEmpty(form.getContactName())){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"请填写联系人姓名!");
        }
        if(form.getDishCateId() <= 0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"请填写经营品类!");
        }
        if(form.getAmount() <= 0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"门店数量必须大于0!");
        }

        return goService.formSubmit(form,getUser());
    }

    /**
     * 运营开店申请获取
     * @return
     */
    @GetMapping(value = "/go/form/get")
    public BizPacket getForm(){
        POIUserData userData = getUser();
        return goService.formGet(userData);
    }


    /**
     * 运营开店申请
     * @param id
     * @return
     */
    @RequestMapping(value = "/go/form/resubmit",method = {RequestMethod.GET,RequestMethod.POST})
    public BizPacket formReSubmit(@RequestParam("id") Integer id){
        if(id == null || id ==0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"id必传");
        }
        return goService.formReSubmit(id);
    }


    @RequestMapping(value = "/go/auth/mobile",method = {RequestMethod.GET,RequestMethod.POST},produces = {"application/json","text/html"})
    public BizPacket wechatAuthByMobile(String name,String mobile,String smsCode){
        String code = redisService.getSMSCode(mobile);
        if(code == null || code.trim().length() ==0 || !code.equalsIgnoreCase(smsCode)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"手机验证码不对!");
        }
        if(StringUtils.isEmpty(name)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"抱歉，请填写姓名!");
        }


        POIUserData userData = getUser();
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
}