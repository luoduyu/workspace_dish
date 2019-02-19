package com.amt.wechat.controller.wechat.bidding;

import com.amt.wechat.common.constants.Constants;
import com.amt.wechat.controller.wechat.base.BaseController;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.WechatUtil;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.service.bidding.BiddingService;
import com.amt.wechat.service.pay.util.WechatXMLParser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  竞价类controller
 *
 * @author adu Create on 2019-01-08 17:11
 * @version 1.0
 */
@RestController
public class BiddingController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(BiddingController.class);

    private @Value("${devMode}") boolean devMode;

    private @Resource BiddingService biddingService;


    /**
     *  竞价充值记录
     * @param index
     * @param pageSize
     * @return
     */
    @PostMapping(value = "/bidding/recharge/in/list")
    public BizPacket getMyRechargeData(Integer index,Integer pageSize){
        if(index == null){
            index = 0;
        }
        if(pageSize== null){
            pageSize = 20;
        }

        if(index<0 || index >= Integer.MAX_VALUE){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"页索引参数非法!");
        }
        if(pageSize<0 || pageSize >= 100){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"页大小参数非法!");
        }


        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }
        return biddingService.getMyRechargeData(userData,index,pageSize);
    }


    /**
     * 竞价充值消费记录
     *
     * @param index
     * @param pageSize
     * @return
     */
    @PostMapping(value = "/bidding/recharge/out/list")
    public BizPacket getMyConsumeData(Integer index,Integer pageSize){
        if(index == null){
            index = 0;
        }
        if(pageSize== null){
            pageSize = 20;
        }

        if(index<0 || index >= Integer.MAX_VALUE){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"页索引参数非法!");
        }
        if(pageSize<0 || pageSize >= 100){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"页大小参数非法!");
        }


        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        return biddingService.getMyConsumeData(userData,index,pageSize);
    }


    /**
     * 竞价充值
     * @param amount
     * @return
     */
    @PostMapping(value = "/bidding/recharge/in")
    public BizPacket recharge(@RequestParam("amount") Long amount){
        if(!devMode){
            if(amount < 10000 || amount>= Integer.MAX_VALUE){
                return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"充值金额不符合规定!");
            }
        }

        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        if(StringUtils.isEmpty(userData.getOpenid())){
            return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(),"必须先完成微信授权!");
        }
        try {
            return biddingService.recharge(userData,amount.intValue());
        } catch (Exception e) {
            logger.error("u="+userData+",amount="+amount+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }


    /**
     * 微信付款回调接口
     */
    @RequestMapping(value = Constants.PAY_CALLBACK_URL_BIDDING,method = {RequestMethod.POST,RequestMethod.GET})
    public void rechargeCallback(HttpServletRequest request, HttpServletResponse response){

        Document doc = null;
        try {

            String responseText = WechatUtil.getResponseText(request);
            logger.info("竞价充值回调responseText={}",responseText);
            doc = WechatUtil.getResponseXML(responseText);

        }catch (IOException | DocumentException e){
            logger.error(e.getMessage(),e);

            // 通信就失败了
            WechatUtil.responseFail(response,Constants.WE_FAIL,null);
            return;
        }

        Map<String,String> result = WechatXMLParser.callbackResultParse(doc.getRootElement());
        // 根据微信的result_code判断微信端是否支付成功,支付成功则执行成功后的后台处理
        if (result == null || !Constants.WE_SUCCESS.equals(result.get("result_code"))) {
            // 虽通信成功,但最终是失败
            WechatUtil.responseFail(response,Constants.WE_SUCCESS,result);
            return;
        }
        try {

            BizPacket bizPacket = biddingService.rechargeCallback(result);
            logger.info("小程序竞价充值结果={}", bizPacket);

            response.getWriter().write(WechatUtil.WECHAT_PAY_CALLBACK_SUCC);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }
}