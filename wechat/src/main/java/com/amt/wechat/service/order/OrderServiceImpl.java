package com.amt.wechat.service.order;

import com.alibaba.fastjson.JSONObject;
import com.amt.wechat.dao.order.OrderDao;
import com.amt.wechat.dao.poi.PoiDao;
import com.amt.wechat.dao.poi.PoiMaterialDao;
import com.amt.wechat.dao.poster.PosterDao;
import com.amt.wechat.domain.id.Generator;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.DateTimeUtil;
import com.amt.wechat.form.order.*;
import com.amt.wechat.model.common.GoodsData;
import com.amt.wechat.model.order.MyOrderForm;
import com.amt.wechat.model.order.OrderData;
import com.amt.wechat.model.order.OrderItemData;
import com.amt.wechat.model.order.OrderServiceData;
import com.amt.wechat.model.poi.MaterialData;
import com.amt.wechat.model.poi.PoiData;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.model.poster.PosterData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-04 19:26
 * @version 1.0
 */
@Service("orderService")
public class OrderServiceImpl implements  OrderService {
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private @Resource OrderDao orderDao;
    private @Resource PosterDao posterDao;
    private @Resource PoiMaterialDao poiMaterialDao;
    private @Resource PoiDao poiDao;

    @Override
    public List<MyOrderForm> getOrderDataList(String poiId, int index, int pageSize) {
        return orderDao.getMyOrderList(poiId,index,pageSize);
    }

    @Override
    public BizPacket getOrderDetail(String orderId) {
        OrderData data = orderDao.getOrder(orderId);
        if(data == null){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"请求的订单不存在!");
        }

        // 订单信息
        MyOrderFormEx form = createOrderFormDetail(data);


        // 订单购买项信息
        List<OrderItemData> itemList = orderDao.getOrderItemList(orderId);
        form.setItemList(itemList);

        if(data.getServiceStatus() == ServiceStatus.NONE.value()){
            return BizPacket.success(form);
        }

        // 订单服务及评价信息
        OrderServiceDataEx osde = createServicDetail(orderId);
        form.setOrderServiceData(osde);

        return BizPacket.success(form);
    }


    private MyOrderFormEx createOrderFormDetail(OrderData data){
        MyOrderFormEx form = new MyOrderFormEx();
        form.setCreateTime(data.getCreateTime());
        form.setGoodsType(data.getGoodsType());
        form.setOrderId(data.getOrderId());
        form.setPayment(data.getPayment());
        form.setPayStatus(data.getPayStatus());
        form.setTotal(data.getTotal());
        form.setBalancePaid(data.getBalancePaid());
        form.setCouponPaid(data.getCouponPaid());
        form.setWechatPaid(data.getWechatPaid());
        form.setPayTime(data.getPayTime());
        form.setServiceStatus(data.getServiceStatus());
        return form;
    }

    /**
     * 获得评价数据
     * @param orderId
     * @return
     */
    private OrderServiceDataEx createServicDetail(String orderId){
        OrderServiceDataEx osde = new OrderServiceDataEx();
        OrderServiceData osd = orderDao.getOrderService(orderId);
        if(osd == null){
            return osde;
        }
        osde.init(osd);


        Integer result = orderDao.sumTotalScore(osd.getServicerId());
        osde.setTotalScore(result == null? 0: result);
        return osde;
    }

    @Override
    public BizPacket commentSubmit(String orderId, Integer scoreService, Integer scoreProfess, Integer scoreResponse, String commentText, PoiUserData userData) {
        try {
            OrderData data = orderDao.getOrder(orderId);
            if(data == null){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"请求的订单不存在!");
            }
            if(data.getServiceStatus() != ServiceStatus.OK.value()){
                return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(),"订单未完成,暂时不接受评价!");
            }
            if(!userData.getPoiId().equalsIgnoreCase(data.getPoiId())){
                return BizPacket.error(HttpStatus.FORBIDDEN.value(),"不能评论不属于自己的店铺!");
            }

            OrderServiceData serviceData = orderDao.getOrderService(data.getOrderId());
            if(serviceData == null){
                return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(),"订单未受理,暂时不接受评价!");
            }

            if(serviceData.getCommentStatus() == 1){
                return BizPacket.error(HttpStatus.CONFLICT.value(),"订单已评价过了!");
            }

            let(serviceData,scoreService, scoreProfess, scoreResponse, commentText, userData.getId());
            orderDao.updateOrderServiceData(serviceData);
            return BizPacket.success();
        } catch (Exception e) {
            logger.error("orderId="+orderId+",userId="+userData.getId()+",scoreService="+scoreService+",scoreProfess="+scoreProfess+",scoreResponse="+scoreResponse+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }


    /**
     * 赋值
     * @param serviceData
     * @param scoreService
     * @param scoreProfess
     * @param scoreResponse
     * @param commentText
     * @param userId
     * @return
     */
    private OrderServiceData let(OrderServiceData serviceData,Integer scoreService, Integer scoreProfess, Integer scoreResponse, String commentText, String userId){
        serviceData.setCommentStatus(1);
        if(commentText != null){
            serviceData.setCommentText(commentText);
        }
        serviceData.setScoreService(scoreService);
        serviceData.setScoreProfess(scoreProfess);
        serviceData.setScoreResponse(scoreResponse);
        serviceData.setCommentUserId(userId);
        serviceData.setCommentTime(DateTimeUtil.now());
        return serviceData;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public BizPacket orderSubmit(PoiUserData userData, OrderSubmitForm orderSubmitForm) {
        try {
            Map<Integer,GoodsData> goodsMap = getGoodsMap(orderSubmitForm);
            if(goodsMap.size() != orderSubmitForm.getOrderItemList().size()){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"提交的物品有误!");
            }

            PoiData poiData = poiDao.getPoiData(userData.getPoiId());
            if(poiData == null){
                return BizPacket.error(HttpStatus.PROXY_AUTHENTICATION_REQUIRED.value(), "需要店铺授权认证!");
            }

            OrderData orderData = createOrderData(userData.getPoiId(),orderSubmitForm.getGoodsType());
            List<OrderItemData> itemList = createItemDataList(poiData.getMemberFlag() ==1,orderData,orderSubmitForm.getOrderItemList(),goodsMap);
            orderDao.addOrderItemDataList(itemList);
            orderDao.addOrderData(orderData);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("totalCost",orderData.getTotal());
            jsonObject.put("totalPayment",orderData.getPayment());
            jsonObject.put("orderId",orderData.getOrderId());

            return BizPacket.success(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }



    /**
     * 构建订单数据
     *
     * @param poiId 店铺Id
     * @param goodsType 物品类型;1:海报;2:装修服务
     * @return
     */
    private OrderData createOrderData(String poiId,int goodsType){
        OrderData orderData = new OrderData();
        orderData.setPoiId(poiId);
        orderData.setBalancePaid(0);
        orderData.setCouponPaid(0);
        orderData.setWechatPaid(0);

        orderData.setCreateTime(DateTimeUtil.now());
        orderData.setGoodsType(goodsType);

        orderData.setPayTime("");
        orderData.setPayNo("");
        orderData.setPayStatus(PayStatus.NOT_PAID.value());
        orderData.setServiceStatus(ServiceStatus.NONE.value());
        orderData.setOrderId(Generator.generate());
        return orderData;
    }

    private List<OrderItemData> createItemDataList(boolean isMember ,OrderData orderData,List<MyOrderItemForm> itemFormList,Map<Integer,GoodsData> goodsMap){
        List<OrderItemData> itemDataList = new ArrayList<>();

        // 总原价
        int totalCost= 0;

        // 实付
        int totalPayment = 0;

        for(MyOrderItemForm e:itemFormList){
            OrderItemData itemData = new OrderItemData();
            GoodsData goodsData = goodsMap.get(e.getGoodsId());

            itemData.setOrderId(orderData.getOrderId());
            itemData.setImgUrl(goodsData.getCoverImg());
            itemData.setGoodsName(goodsData.getName());
            itemData.setGoodsType(orderData.getGoodsType());
            itemData.setGoodsId(goodsData.getId());

            itemData.setUnitPrice(isMember? goodsData.getMemberPrice():goodsData.getPrice());
            itemData.setNum(e.getNum());
            int total = itemData.getUnitPrice() * itemData.getNum();
            itemData.setTotal(total);
            itemDataList.add(itemData);

            totalCost += e.getNum() * goodsData.getPrice();
            totalPayment += total;

        }
        orderData.setTotal(totalCost);
        orderData.setPayment(totalPayment);
        return itemDataList;
    }

    /**
     * 获得售卖的资源(TODO 替换为 基于 redis 的实现?)
     * @param orderSubmitForm
     * @return
     */
    private Map<Integer,GoodsData> getGoodsMap(OrderSubmitForm orderSubmitForm){
        StringBuilder ids = new StringBuilder("");
        for(MyOrderItemForm o:orderSubmitForm.getOrderItemList()){
            ids.append(o.getGoodsId()).append(",");
        }
        ids.deleteCharAt(ids.length()-1);
        Map<Integer,GoodsData> returnMap = new HashMap<>();

        switch (orderSubmitForm.getGoodsType()){
            case 1:
                Map<Integer, PosterData>  posterMap=  posterDao.getPosterDataMap(ids.toString());
                for(Map.Entry<Integer,PosterData> entry:posterMap.entrySet()){
                    returnMap.put(entry.getKey(), entry.getValue());
                }
                return returnMap;

            case 2:
                Map<Integer,MaterialData> materialDataMap = poiMaterialDao.getPoiMaterialDataMap(ids.toString());
                for(Map.Entry<Integer,MaterialData> entry:materialDataMap.entrySet()){
                    returnMap.put(entry.getKey(), entry.getValue());
                }
                return returnMap;

            default:
                return Collections.emptyMap();
        }
    }


    @Override
    public BizPacket orderEditNum(PoiUserData userData, OrderItemForm orderItemForm){
        OrderData orderData = orderDao.getOrder(orderItemForm.getOrderId());
        if(orderData == null){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"订单项不存在!");
        }
        if(orderData.getPayStatus() == PayStatus.PAIED.value()){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"订单已不可编辑!");
        }

        if(!orderData.getPoiId().equalsIgnoreCase(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你的当前店铺与订单所属店铺不一致!");
        }

        List<OrderItemData> itemList =  orderDao.getOrderItemList(orderItemForm.getOrderId());
        if(itemList == null || itemList.isEmpty()){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"订单不存在!");
        }
        itemList = orderItemForm.let(itemList);

        for(OrderItemData o:itemList){
            orderDao.updateOrderItemData(o);
        }
        return BizPacket.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BizPacket orderItemAdd(PoiUserData userData ,String orderId, int goodsType,Integer goodsId, int num){
        try {
            if(StringUtils.isEmpty(userData.getPoiId())){
                return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(),"此项服务不面向个人出售!");
            }
            PoiData poiData = poiDao.getPoiData(userData.getPoiId());
            if(poiData == null){
                return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(),"此项服务不面向个人出售!");
            }

            OrderData orderData = orderDao.getOrder(orderId);
            if(orderData == null){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"订单不存在!");
            }
            if(!orderData.getPoiId().equalsIgnoreCase(userData.getPoiId())){
                return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你的当前店铺与订单所属店铺不一致!");
            }

            if(orderData.getPayStatus() == PayStatus.PAIED.value()){
                return BizPacket.error(HttpStatus.FORBIDDEN.value(),"订单已不可编辑!");
            }

            GoodsData goodsData = getGoodsData(goodsType,goodsId);
            if(goodsData == null){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"物品不存在!");
            }

            // 构建订单项
            OrderItemData data = createItemData(orderData,goodsType,goodsData,num,poiData.getMemberFlag() ==1);
            int orderItemId = orderDao.addOrderItemData(data);



            // 变更订单相应的总价
            orderData.setTotal(orderData.getTotal() + data.getTotal());
            orderDao.updateTotal(orderData.getOrderId(),orderData.getTotal());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",orderItemId);
            jsonObject.put("goodsName",goodsData.getName());
            jsonObject.put("imgUrl",goodsData.getCoverImg());

            return BizPacket.success(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    private OrderItemData createItemData(OrderData orderData,int goodsType,GoodsData goodsData,int num,boolean isMember){
        OrderItemData data = new OrderItemData();
        data.setNum(num);
        data.setGoodsId(goodsData.getId());
        data.setGoodsName(goodsData.getName());
        data.setGoodsType(goodsType);
        data.setOrderId(orderData.getOrderId());


        if(isMember){
            int total =  num * goodsData.getPrice();
            data.setTotal(total);
            data.setUnitPrice(goodsData.getPrice());
        }else{
            int total = num * goodsData.getMemberPrice();
            data.setUnitPrice(goodsData.getMemberPrice());
        }
        data.setImgUrl(goodsData.getCoverImg());
        return data;
    }


    /**
     *
     * @param goodsType 1:海报;2:装修服务
     * @param goodsId
     * @return
     */
    private GoodsData getGoodsData(int goodsType,Integer goodsId){
        switch (goodsType){
            case 1:
                return posterDao.getPosterData(goodsId);

            case 2:
                return poiMaterialDao.getMaterialData(goodsId);

            default:
                return null;
        }
    }

    @Override
    public BizPacket orderItemRM(PoiUserData userData,String orderId,Integer goodsId, int orderItemId){
        try {
            OrderData orderData = orderDao.getOrder(orderId);
            if(orderData == null){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"订单不存在!");
            }

            if(!userData.getPoiId().equalsIgnoreCase(orderData.getPoiId())){
                return BizPacket.error(HttpStatus.FORBIDDEN.value(),"非法操作:只能删除自己店铺的订单!");
            }

            if(orderData.getPayStatus() == PayStatus.PAIED.value()){
                return BizPacket.error(HttpStatus.FORBIDDEN.value(),"订单已不可编辑!");
            }

            OrderItemData data = orderDao.getOrderItem(orderItemId);
            if(data == null){
                return BizPacket.success();
            }
            if(data.getGoodsId() != goodsId){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"订单项不存在!");
            }

            orderDao.deleteOrderItemData(orderItemId);
            return BizPacket.success();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BizPacket orderRM(PoiUserData userData, String orderId) {
        OrderData orderData = orderDao.getOrder(orderId);
        if(orderData == null){
            return BizPacket.success();
        }

        if(!orderData.getPoiId().equalsIgnoreCase(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"非法操作:只能删除自己店铺的订单!");
        }
        if(orderData.getPayStatus() !=  PayStatus.NOT_PAID.value()){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"订单已不可删除!");
        }

        orderDao.removeOrderItemByOrderId(orderId);
        orderDao.removeOrder(orderData.getOrderId());

        return BizPacket.success();
    }
}