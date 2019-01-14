package com.amt.wechat.controller.poi;

import com.amt.wechat.controller.base.BaseController;
import com.amt.wechat.dao.member.MemberDao;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.model.member.MemberCardData;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.service.poi.PoiService;
import com.amt.wechat.service.redis.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  店铺会员类controller
 *
 * @author adu Create on 2019-01-10 14:24
 * @version 1.0
 */
@RestController
public class MemberController extends BaseController {

    private @Resource MemberDao memberDao;
    private @Resource PoiService poiService;
    private @Resource RedisService redisService;

    @GetMapping(value = "/card/list")
    public BizPacket cardList(){
        List<MemberCardData> cardDataList =  memberDao.getMemberCardList();
        return BizPacket.success(cardDataList);
    }

    /**
     *  '我'会员数据获取
     * @return
     */
    @RequestMapping(value = "/member/my/get",method ={RequestMethod.POST,RequestMethod.GET})
    public BizPacket myMemberData(){
        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }
        return poiService.memberDataFetch(userData);
    }


    /**
     * 会员购买记录
     * @param index
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/member/bought/rd/list",method = {RequestMethod.POST,RequestMethod.GET})
    public BizPacket memberBoughtRD(@RequestParam("index") Integer index,  @RequestParam("pageSize") Integer pageSize){
        if(index == null){
            index = 0;
        }
        if(index <0 || index >= Integer.MAX_VALUE){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"页索引参数非法！");
        }

        if(pageSize <=0 || pageSize >= 999999){
            pageSize = 20;
        }

        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }
        return poiService.memberBoughtRD(userData,index,pageSize);
    }


    /**
     * 会员购买
     * @param memberCardId
     * @param feeRenew 是否自动续期；0:否,1:是
     * @return
     */
    @RequestMapping(value = "/member/buy",method = {RequestMethod.POST})
    public BizPacket memberBuy(@RequestParam("memberCardId") Integer memberCardId){

        if(memberCardId == null || memberCardId <0 || memberCardId >= Integer.MAX_VALUE){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"会员卡编号非法！");
        }

        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }
        return poiService.memberBuy(userData,memberCardId,feeRenew);
    }


    /**
     * 会员自动续期取消
     * @param smsCode
     * @return
     */
    @RequestMapping(value = "/member/renew/cancel")
    public BizPacket autoFeeRenewCencel(String smsCode){
        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        String code = redisService.getSMSCode(userData.getMobile());
        if(code == null || code.trim().length() ==0 || !code.equalsIgnoreCase(smsCode)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"手机验证码不对!");
        }

        return poiService.autoFeeRenewCencel(userData);
    }
}