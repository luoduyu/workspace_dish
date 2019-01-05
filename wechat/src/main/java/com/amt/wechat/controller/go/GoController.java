package com.amt.wechat.controller.go;

import com.amt.wechat.controller.base.BaseController;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.form.ShenQingForm;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.service.go.GoService;
import com.amt.wechat.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Value;
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

    private @Value("${devMode}") boolean devMode = false;

    private @Resource RedisService redisService;
    private @Resource GoService goService;

    /**
     * 开店申请提交
     * @param shenQingForm
     * @return
     */
    @RequestMapping(value = "/go/kaidian/submit",method ={RequestMethod.POST,RequestMethod.GET},produces = {"application/json","text/html"})
    public BizPacket kaidianSubmit(ShenQingForm shenQingForm){
        if(!devMode) {
            String code = redisService.getSMSCode(shenQingForm.getContactMobile());
            if (code == null || code.trim().length() == 0 || !code.equalsIgnoreCase(shenQingForm.getSmsCode())) {
                return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "手机验证码不对!");
            }
        }

        if(StringUtils.isEmpty(shenQingForm.getBrandName())){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"品牌名称为必填项!");
        }
        if(StringUtils.isEmpty(shenQingForm.getContactName())){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"请填写联系人姓名!");
        }
        if(shenQingForm.getDishCateId() <= 0){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"请填写经营品类!");
       }

        // 由此地址提交的就是开店申请
        shenQingForm.setUsefor(0);
        return goService.requestFormSubmit(shenQingForm,getUser());
    }

    /**
     * 运营申请提交
     * @param shenQingForm
     * @return
     */
    @RequestMapping(value = "/go/yunying/submit",method ={RequestMethod.POST,RequestMethod.GET},produces = {"application/json","text/html"})
    public BizPacket yunYingSubmit(ShenQingForm shenQingForm){
        if(!devMode) {
            String code = redisService.getSMSCode(shenQingForm.getContactMobile());
            if (code == null || code.trim().length() == 0 || !code.equalsIgnoreCase(shenQingForm.getSmsCode())) {
                return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "手机验证码不对!");
            }
        }

        if(StringUtils.isEmpty(shenQingForm.getBrandName())){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"品牌名称为必填项!");
        }
        if(StringUtils.isEmpty(shenQingForm.getContactName())){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"请填写联系人姓名!");
        }

        // 由此地址提交的就是运营申请
        shenQingForm.setUsefor(1);
        return goService.requestFormSubmit(shenQingForm,getUser());
    }

    /**
     * 开店申请获取
     * @return
     */
    @GetMapping(value = "/go/kaidian/get")
    public BizPacket kaidianFormGet(){
        PoiUserData userData = getUser();
        return goService.requestFormGet(userData,0);
    }

    /**
     * 运营申请获取
     * @return
     */
    @GetMapping(value = "/go/yunying/get")
    public BizPacket yunyingFormGet(){
        PoiUserData userData = getUser();
        return goService.requestFormGet(userData,1);
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
        return goService.requestFormReSubmit(id);
    }
}