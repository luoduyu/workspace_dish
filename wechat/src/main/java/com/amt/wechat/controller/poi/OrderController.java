package com.amt.wechat.controller.poi;

import com.amt.wechat.controller.base.BaseController;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.form.order.MyOrderItemForm;
import com.amt.wechat.form.order.OrderItemForm;
import com.amt.wechat.form.order.OrderSubmitForm;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.service.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    private @Resource OrderService orderService;

    @PostMapping(value = "/order/my")
    public BizPacket findOrderList(Integer index,Integer pageSize){
        if(index == null){
            index = 0;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        PoiUserData userData = getUser();

        return orderService.getOrderDataList(userData.getPoiId(),index,pageSize);
    }

    @PostMapping(value = "/order/detail")
    public BizPacket findOrderDetail(@RequestParam("orderId") String orderId){
        if(StringUtils.isEmpty(orderId)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"订单Id为必填项!");
        }

        BizPacket packet =  orderService.getOrderDetail(orderId);
        return packet;
    }


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


    @PostMapping(value = "/order/submit",produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public BizPacket orderSubmit(OrderSubmitForm orderSubmitForm){
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

        return orderService.orderSubmit(userData,orderSubmitForm);
    }


    @PostMapping(value = "/order/rm",produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public BizPacket orderRM(String orderId){
        if(StringUtils.isEmpty(orderId)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"订单Id是必须参数!");
        }
        PoiUserData userData = getUser();
        if(StringUtils.isEmpty(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"非法操作:你不属于任何商户!");
        }

        return orderService.orderRM(userData,orderId);
    }

    @PostMapping(value = "/order/item/num",produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public BizPacket orderEditNum(OrderItemForm orderItemForm){
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

        BizPacket packet =  orderService.orderEditNum(userData,orderItemForm);
        if(packet == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"参数非法!");
        }
        return packet;
    }



    @PostMapping(value = "/order/item/add",produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
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
        return orderService.orderItemAdd(userData,orderId,goodsType,goodsId,num);
    }

    @PostMapping(value = "/order/item/rm",produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
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

        return orderService.orderItemRM(userData,orderId,goodsId,id);
    }
}