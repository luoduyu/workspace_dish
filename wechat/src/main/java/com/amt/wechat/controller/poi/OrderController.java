package com.amt.wechat.controller.poi;

import com.amt.wechat.common.Constants;
import com.amt.wechat.common.PayWay;
import com.amt.wechat.controller.base.BaseController;
import com.amt.wechat.dao.poi.PoiDao;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.WechatUtil;
import com.amt.wechat.form.order.MyOrderItemForm;
import com.amt.wechat.form.order.OrderItemForm;
import com.amt.wechat.form.order.OrderSubmitForm;
import com.amt.wechat.model.poi.PoiData;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.service.order.OrderService;
import com.amt.wechat.service.pay.util.WechatXMLParser;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
 * 订单类controller
 *
 * @author adu Create on 2019-01-04 19:28
 * @version 1.0
 */
@RestController
public class OrderController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(OrderController.class);
    private @Resource OrderService orderService;
    private @Resource PoiDao poiDao;


    @PostMapping(value = "/order/my")
    public BizPacket findOrderList(Integer index,Integer pageSize){
        if(index == null){
            index = 0;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        PoiUserData userData = getUser();

        return orderService.getOrderDataList(userData,index,pageSize);
    }

    @PostMapping(value = "/order/detail")
    public BizPacket findOrderDetail(@RequestParam("orderId") String orderId){
        if(StringUtils.isEmpty(orderId)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"订单Id为必填项!");
        }

        PoiUserData userData = getUser();

        BizPacket packet =  orderService.getOrderDetail(userData,orderId);
        return packet;
    }


    /**
     * 订单评论提交
     * @param orderId
     * @param scoreService
     * @param scoreProfess
     * @param scoreResponse
     * @param commentText
     * @return
     */
    @PostMapping(value = "/order/comment/submit")
    public BizPacket commentSubmit(String orderId,Integer scoreService,Integer scoreProfess,Integer scoreResponse,String commentText){
        if(StringUtils.isEmpty(orderId)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"订单Id是必传参数!");
        }
        if(scoreService == null){
            scoreService = 1;
        }
        if(scoreProfess == null){
            scoreProfess = 1;
        }
        if(scoreResponse == null){
            scoreResponse = 1;
        }

        if(StringUtils.isEmpty(commentText)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"请填写评价内容,谢谢!");
        }
        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"非法操作:你不属于任何商户!");
        }
        return orderService.commentSubmit(orderId,scoreService,scoreProfess,scoreResponse,commentText,getUser());
    }


    @PostMapping(value = "/order/submit",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BizPacket orderSubmit(@RequestBody OrderSubmitForm orderSubmitForm){
        if(orderSubmitForm == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"未收到订单参数!");
        }
        if(orderSubmitForm.getOrderItemList() == null || orderSubmitForm.getOrderItemList().isEmpty()){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"订单数据不完整!");
        }
        for(MyOrderItemForm o:orderSubmitForm.getOrderItemList()){
            if(o.getNum() <= 0 ){
                return BizPacket.error(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value(),"购买数量非法!");
            }
        }

        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"非法操作:你不属于任何商户!");
        }

        try {
            return orderService.orderSubmit(userData,orderSubmitForm);
        } catch (Exception e) {
            logger.error("userData="+userData+",orderSubmitForm="+orderSubmitForm+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    /**
     * 订单付款确认
     * @param orderId 订单Id
     * @return
     */
    @PostMapping(value = "/order/pay/confirm")
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
            BizPacket ret= orderService.payConfirm(userData,orderId,pw);
            return ret;
        } catch (Exception e) {
            logger.error("userData="+userData+",orderId="+orderId+",payWay="+payWay+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    /**
     * 微信付款回调接口
     */
    @RequestMapping(value = Constants.PAY_CALLBACK_URL_ORDER,method = {RequestMethod.POST,RequestMethod.GET})
    public void memberBuyCallback(HttpServletRequest request, HttpServletResponse response){

        Document doc = null;
        try {

            String responseText = WechatUtil.getResponseText(request);
            logger.info("订单付款回调responseText={}",responseText);
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

            BizPacket bizPacket = orderService.payCallback(result);
            logger.info("订单付回调处理结果={}", bizPacket);

            response.getWriter().write(WechatUtil.WECHAT_PAY_CALLBACK_SUCC);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
    }


    @PostMapping(value = "/order/rm")
    public BizPacket orderRM(String orderId){
        if(StringUtils.isEmpty(orderId)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"订单Id是必须参数!");
        }
        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"非法操作:你不属于任何商户!");
        }

        try {
            return orderService.orderRM(userData,orderId);
        } catch (Exception e) {
            logger.error("userData="+userData+",orderId="+orderId+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @PostMapping(value = "/order/item/num",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BizPacket orderEditNum(@RequestBody OrderItemForm orderItemForm){
        if(orderItemForm == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"缺少必要的参数!");
        }
        if(!orderItemForm.validate()){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"缺少必要的参数!");
        }

        PoiUserData userData =getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"非法操作:你不属于任何商户!");
        }

        try {
            BizPacket packet =  orderService.orderEditNum(userData,orderItemForm);
            if(packet == null){
                return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"参数非法!");
            }
            return packet;
        } catch (Exception e) {
            logger.error("userData="+userData+",orderItemForm="+orderItemForm+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }



    @PostMapping(value = "/order/item/add")
    public BizPacket orderItemAdd(@RequestParam("orderId") String orderId, @RequestParam("goodsType") Integer goodsType, @RequestParam("goodsId") Integer goodsId, @RequestParam("num") Integer num){
        if(StringUtils.isEmpty(orderId)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"订单Id是必须参数!");
        }

        if(StringUtils.isEmpty(goodsId)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"物品Id是必须参数!");
        }

        if(StringUtils.isEmpty(num)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"数量是必须参数!");
        }
        if(num <=0 || num >= 999999){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"数量参数错误!");
        }
        if(goodsType != 1 && goodsType != 2 && goodsType != 3){
            return   BizPacket.error(HttpStatus.BAD_REQUEST.value(),"物品类型参数错误!");
        }

        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"非法操作:你不属于任何商户!");
        }
        try {
            return orderService.orderItemAdd(userData,orderId,goodsType,goodsId,num);
        } catch (Exception e) {
            logger.error("userData="+userData+",orderId="+orderId+",goodsType="+goodsType+",goodsId="+goodsId+",num="+num+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @PostMapping(value = "/order/item/rm")
    public BizPacket orderEdit(@RequestParam("orderId") String orderId, @RequestParam("goodsId") Integer goodsId, @RequestParam("id") Integer id){
        if(StringUtils.isEmpty(orderId)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"订单Id是必须参数!");
        }

        if(StringUtils.isEmpty(goodsId)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"物品Id是必须参数!");
        }

        if(StringUtils.isEmpty(id)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"订单项序号id是必须参数!");
        }
        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"非法操作:你不属于任何商户!");
        }

        try {
            return orderService.orderItemRM(userData,orderId,goodsId,id);
        } catch (Exception e) {
            logger.error("userData="+userData+",orderId="+orderId+",goodsId="+goodsId+",id="+id+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }
}