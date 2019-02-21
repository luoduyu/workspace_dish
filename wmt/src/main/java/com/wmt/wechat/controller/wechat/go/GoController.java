package com.wmt.wechat.controller.wechat.go;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.wechat.controller.wechat.base.BaseController;
import com.wmt.wechat.form.yunying.ShenQingForm;
import com.wmt.wechat.model.poi.PoiUserData;
import com.wmt.wechat.service.go.GoService;
import com.wmt.wechat.service.go.ShenQingType;
import com.wmt.wechat.service.redis.RedisService;
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
    private @Resource GoService goService;

    /**
     * 开店申请提交
     * @param shenQingForm
     * @return
     */
    @RequestMapping(value = "/go/kaidian/submit",method ={RequestMethod.POST,RequestMethod.GET},produces = {"application/json","text/html"})
    public BizPacket kaidianSubmit(ShenQingForm shenQingForm){

        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getMobile())){
            return BizPacket.error(HttpStatus.UNAUTHORIZED.value(),"缺少店铺认证!");
        }

        String code = redisService.getSMSCode(userData.getMobile());
        if (code == null || code.trim().length() == 0 || !code.equalsIgnoreCase(shenQingForm.getSmsCode())) {
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "手机验证码不对!");
        }

        if(StringUtils.isEmpty(shenQingForm.getBrandName())){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"品牌名称为必填项!");
        }

        if(shenQingForm.getDishCateId() <= 0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"请填写经营品类!");
        }
        return goService.requestFormSubmit(shenQingForm,userData, ShenQingType.KAIDIAN);
    }

    /**
     * 运营申请提交
     * @param shenQingForm
     * @return
     */
    @RequestMapping(value = "/go/yunying/submit",method ={RequestMethod.POST,RequestMethod.GET},produces = {"application/json","text/html"})
    public BizPacket yunYingSubmit(ShenQingForm shenQingForm){
        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getMobile())){
            return BizPacket.error(HttpStatus.UNAUTHORIZED.value(),"缺少店铺认证!");
        }

        String code = redisService.getSMSCode(userData.getMobile());
        if (code == null || code.trim().length() == 0 || !code.equalsIgnoreCase(shenQingForm.getSmsCode())) {
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "手机验证码不对!");
        }
        if(StringUtils.isEmpty(shenQingForm.getBrandName())){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"品牌名称为必填项!");
        }

        return goService.requestFormSubmit(shenQingForm,userData,ShenQingType.YUNYING);
    }

    /**
     * 开店申请获取
     * @return
     */
    @GetMapping(value = "/go/kaidian/get")
    public BizPacket kaidianFormGet(){
        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        return goService.requestFormGet(userData,ShenQingType.KAIDIAN);
    }

    /**
     * 运营申请获取
     * @return
     */
    @GetMapping(value = "/go/yunying/get")
    public BizPacket yunyingFormGet(){
        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }
        return goService.requestFormGet(userData,ShenQingType.YUNYING);
    }


    /**
     * 开店/运营申请重提交
     * @param id
     * @return
     */
    @RequestMapping(value = "/go/requestform/resubmit",method = {RequestMethod.GET,RequestMethod.POST})
    public BizPacket formReSubmit(@RequestParam("id") Integer id){
        if(id == null || id ==0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"id必传");
        }
        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        return goService.requestFormReSubmit(userData,id);
    }
}