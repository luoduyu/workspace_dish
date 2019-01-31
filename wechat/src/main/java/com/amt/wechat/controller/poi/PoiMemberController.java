package com.amt.wechat.controller.poi;

import com.amt.wechat.common.Constants;
import com.amt.wechat.common.PayWay;
import com.amt.wechat.controller.base.BaseController;
import com.amt.wechat.dao.member.MemberDao;
import com.amt.wechat.dao.poi.PoiDao;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.WechatUtil;
import com.amt.wechat.model.member.MemberCardData;
import com.amt.wechat.model.poi.PoiData;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.service.pay.util.WechatXMLParser;
import com.amt.wechat.service.poi.PoiMemberService;
import com.amt.wechat.service.redis.RedisService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  店铺会员类controller
 *
 * @author adu Create on 2019-01-10 14:24
 * @version 1.0
 */
@RestController
public class PoiMemberController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(PoiMemberController.class);

    private @Resource MemberDao memberDao;
    private @Resource PoiMemberService poiMemberService;
    private @Resource RedisService redisService;
    private @Resource PoiDao poiDao;

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
        return poiMemberService.memberDataFetch(userData);
    }


    @PostMapping(value = "/member/bought/rd")
    public BizPacket memberBoughtRD(@RequestParam("orderId")  String orderId){

        if(StringUtils.isEmpty(orderId)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"订单Id必传!");
        }
        PoiUserData userData = getUser();

        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        return poiMemberService.memberBoughtRD(userData,orderId);
    }

    /**
     * 会员购买记录
     * @param index
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/member/bought/rd/list",method = {RequestMethod.POST,RequestMethod.GET})
    public BizPacket memberBoughtRDList(@RequestParam("index") Integer index,  @RequestParam("pageSize") Integer pageSize){
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
        return poiMemberService.memberBoughtRDList(userData,index,pageSize);
    }


    /**
     * 会员购买(前的预览)
     * @param memberCardId
     * @param feeRenew 是否自动续期；0:否,1:是
     * @return
     */
    @PostMapping(value = "/member/buy/submit")
    public BizPacket memberBuySubmit(@RequestParam("memberCardId") Integer memberCardId,Integer feeRenew){

        if(memberCardId == null || memberCardId <0 || memberCardId >= Integer.MAX_VALUE){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"会员卡编号非法！");
        }
        if(feeRenew == null){
            feeRenew = 1;
        }

        if(feeRenew != 0 && feeRenew != 1){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"'是否续期'参数非法！");
        }

        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }
        try {
            BizPacket ret= poiMemberService.memberBuyPreview(userData,memberCardId,feeRenew);
            return ret;
        } catch (Exception e) {
           logger.error("userData="+userData+",memberCardId="+memberCardId+",feeRenew="+feeRenew+",e="+e.getMessage(),e);
           return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }



    /**
     * 会员购买确认
     * @param orderId 订单Id
     * @return
     */
    @PostMapping(value = "/member/buy/confirm")
    public BizPacket memberBuyConfirm(@RequestParam("orderId") String orderId,Integer payWay,String balancePwd){
        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        PayWay pw = PayWay.valueOf(payWay);
        if(pw == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"付款方式参数非法:"+payWay);
        }
        if(pw != PayWay.WECHAT && pw != PayWay.BALANCE){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"暂时不支持其它付款方式:"+pw.toString());
        }
        if(pw == PayWay.BALANCE){
            PoiData poiData = poiDao.getPoiData(userData.getPoiId());
            if(poiData.getBalancePwdFree() != 1){
                if(StringUtils.isEmpty(balancePwd)){
                    return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"余额密码是必填!");
                }
                if(!balancePwd.equals(poiData.getBalancePwd())){
                    return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"密码错误!");
                }
            }
        }

        try {
            BizPacket ret= poiMemberService.memberBuyConfirm(userData,orderId,pw);
            return ret;
        } catch (Exception e) {
            logger.error("userData="+userData+",orderId="+orderId+",payWay="+payWay+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    /**
     * 微信付款回调接口
     */
    @RequestMapping(value = Constants.PAY_CALLBACK_URL_MEMBER_BUY,method = {RequestMethod.POST,RequestMethod.GET})
    public void memberBuyCallback(HttpServletRequest request, HttpServletResponse response){

        Document doc = null;
        try {

            String responseText = WechatUtil.getResponseText(request);
            logger.info("会员购买回调responseText={}",responseText);
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
            // TODO 拿到一个订单的分布式锁(基于redis)

            BizPacket bizPacket = poiMemberService.memberBuyCallback(result);
            logger.info("会员购买回调处理结果={}", bizPacket);

            response.getWriter().write(WechatUtil.WECHAT_PAY_CALLBACK_SUCC);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
    }

    @PostMapping(value = "/member/autofree/set")
    public BizPacket freeRenewSet(@RequestParam("feeRenew") Integer feeRenew){
        if(feeRenew == null){
            feeRenew = 1;
        }

        if(feeRenew != 0 && feeRenew != 1){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"是否续期参数非法！");
        }

        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }
        return poiMemberService.feeRenewSet(userData,feeRenew);

    }


    /**
     * 会员自动续期取消
     * @param smsCode
     * @return
     */
    @PostMapping(value = "/member/renew/cancel")
    public BizPacket autoFeeRenewCencel(String smsCode){
        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }

        String code = redisService.getSMSCode(userData.getMobile());
        if(code == null || code.trim().length() ==0 || !code.equalsIgnoreCase(smsCode)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"手机验证码不对!");
        }

        return poiMemberService.autoFeeRenewCencel(userData);
    }


    /**
     * 会员自动续期取消
     *
     * @return
     */
    @PostMapping(value = "/member/feedback")
    public BizPacket memberFeedback(String svcQty,String suggestText){
        PoiUserData userData = getUser();

        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
        }
        if(StringUtils.isEmpty(svcQty)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"缺少'服务质量'");
        }
        if(StringUtils.isEmpty(suggestText)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"缺少'建议文本'");
        }
        if(svcQty.trim().length() <= 1 || svcQty.trim().length() > 20){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"'服务质量'太长!");
        }

        if(suggestText.trim().length() <= 1 || suggestText.trim().length() > 200){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"'建议文本'太长!");
        }

        return poiMemberService.memberFeedback(userData,svcQty.trim(),suggestText.trim());
    }
}