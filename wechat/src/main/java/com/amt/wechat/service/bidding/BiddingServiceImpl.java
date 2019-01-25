package com.amt.wechat.service.bidding;

import com.alibaba.fastjson.JSONObject;
import com.amt.wechat.common.Constants;
import com.amt.wechat.common.PayStatus;
import com.amt.wechat.common.PayWay;
import com.amt.wechat.dao.bidding.BiddingDao;
import com.amt.wechat.dao.poi.PoiAccountDao;
import com.amt.wechat.dao.poi.PoiDao;
import com.amt.wechat.domain.id.Generator;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.DateTimeUtil;
import com.amt.wechat.model.bidding.BiddingConsumeRd;
import com.amt.wechat.model.bidding.BiddingRechargeRd;
import com.amt.wechat.model.poi.PoiAccountData;
import com.amt.wechat.model.poi.PoiData;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.service.pay.PayWechatService;
import com.amt.wechat.service.pay.util.MD5Util;
import com.amt.wechat.service.pay.util.Sha1Util;
import com.amt.wechat.service.pay.util.WechatXMLParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("biddingService")
public class BiddingServiceImpl implements  BiddingService{
    private static Logger logger = LoggerFactory.getLogger(BiddingServiceImpl.class);

    private @Resource BiddingDao biddingDao;
    private @Resource PoiDao poiDao;
    private @Resource PoiAccountDao poiAccountDao;
    private @Resource PayWechatService payWechatService;

    @Override
    public BizPacket getMyRechargeData(PoiUserData userData, int index, int pageSize) {
        try {
            PoiData poiData = poiDao.getPoiData(userData.getPoiId());
            if(poiData == null){
                return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
            }


            int total = biddingDao.countRechargeData(poiData.getId());
            if(total <= 0){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"您还没有充过值!");
            }
            List<BiddingRechargeRd> list = biddingDao.getRechargeDataList(poiData.getId(),index * pageSize,pageSize);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total",total);
            jsonObject.put("list",list);
            return BizPacket.success(jsonObject);
        } catch (Exception e) {
           logger.error("usr="+userData+",e="+e.getMessage(),e);
           return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }


    @Override
    public BizPacket getMyConsumeData(PoiUserData userData, int index, int pageSize){
        try {
            PoiData poiData = poiDao.getPoiData(userData.getPoiId());
            if(poiData == null){
                return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
            }


            int total = biddingDao.countConsumeData(poiData.getId());
            if(total <= 0){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"暂时还没有消费记录!");
            }
            List<BiddingConsumeRd> list = biddingDao.getConsumeDataList(poiData.getId(),index * pageSize,pageSize);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total",total);
            jsonObject.put("list",list);
            return BizPacket.success(jsonObject);
        } catch (Exception e) {
            logger.error("usr="+userData+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    private static final String RECHARGE_ID = "rdId=";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BizPacket recharge(PoiUserData userData, int amount) throws Exception {
        BiddingRechargeRd rd = createRechargeRd(userData,PayWay.WECHAT,amount,-1);
        biddingDao.addRechargeRd(rd);

        String nonce_str = Sha1Util.getNonceStr();
        String body = buildBody(amount);
        String attach = RECHARGE_ID +rd.getId();

        BizPacket bizPacket = payWechatService.prePayOrder(userData.getOpenid(),nonce_str,body,attach,rd.getOrderId(),amount, Constants.PAY_CALLBACK_URL_ALL_BIDDING);
        logger.info("竞价充值--统一下单结果={}",bizPacket.toString());

        if(bizPacket.getCode() != HttpStatus.OK.value()){
            throw new Exception(bizPacket.getMsg());
        }


        List<NameValuePair> signParams = new ArrayList<>(5);
        signParams.add(new BasicNameValuePair("appId", Constants.WEICHAT_APP_ID));
        signParams.add(new BasicNameValuePair("nonceStr", nonce_str));

        String sPackage = "prepay_id="+bizPacket.getData().toString();
        signParams.add(new BasicNameValuePair("package", sPackage));

        signParams.add(new BasicNameValuePair("signType", "MD5"));
        String timestamp = DateTimeUtil.getTimeSeconds();
        signParams.add(new BasicNameValuePair("timeStamp",timestamp));

        String paySign = MD5Util.makeSign(signParams,Constants.WECHAT_API_KEY);

        JSONObject prePayParams = new JSONObject();
        prePayParams.put("timeStamp",timestamp);
        prePayParams.put("nonceStr",nonce_str);
        prePayParams.put("package",sPackage);
        prePayParams.put("signType","MD5");
        prePayParams.put("paySign",paySign);

        logger.info("用户[{}]竞价充值,充值记录={},nonce_str={},prepay_id={}",userData,rd,nonce_str,bizPacket.getData().toString());
        return BizPacket.success(prePayParams);
    }

    private String buildBody(int amount){
        return "poi-recharge-"+amount+"fen";
    }



    /**
     * 充值rd
     * @param userData
     * @param amount
     * @param currentTotalBiddingBalance
     * @return
     */
    private BiddingRechargeRd createRechargeRd(PoiUserData userData, PayWay payWay, int amount, int currentTotalBiddingBalance){
        BiddingRechargeRd rd = new BiddingRechargeRd();
        rd.setAmount(amount);
        rd.setBalance(currentTotalBiddingBalance);
        rd.setCreateTime(DateTimeUtil.now());
        rd.setPoiId(userData.getPoiId());
        rd.setUserId(userData.getId());
        rd.setUserName(userData.getName());
        rd.setPayStatus(PayStatus.NOT_PAID.value());
        rd.setOrderId(Generator.generate());
        rd.setPayWay(payWay.value());
        rd.setTransactionId("");
        rd.setSummary("");
        rd.setTimeEnd("");
        return rd;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public BizPacket rechargeCallback(Map<String, String> wechatPayCallbackParams) {
        String attch = wechatPayCallbackParams.get("attach");
        if(StringUtils.isEmpty(attch)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"竞价充值回调中缺少attch(rechargeId):null");
        }

        String rdId= attch.replace(RECHARGE_ID,"");
        BiddingRechargeRd rd = biddingDao.getRechargeData(rdId);
        if(rd == null) {
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "根据rechargeId未能找到充值记录!attch(rdId)=" + rdId);
        }

        String orderId = wechatPayCallbackParams.get("orderId");
        if(StringUtils.isEmpty(orderId)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "竞价充值回调中缺少orderId="+orderId);
        }
        if(!rd.getOrderId().equals(orderId)){
            return BizPacket.error(HttpStatus.CONFLICT.value(), "竞价充值回调中的参数冲突(分别代表了不同的订单)!orderId="+orderId+",rdId="+rdId);
        }


        rd.setPayStatus(PayStatus.PAIED.value());
        rd.setTransactionId(wechatPayCallbackParams.get("transactionId"));

        String summary = WechatXMLParser.joinSummary(wechatPayCallbackParams);
        rd.setSummary(summary);


        // TODO 请求一个 '店铺帐户全局分布式锁’(基于redis)
        PoiAccountData accountData =  poiAccountDao.getAccountData(rd.getPoiId());
        int amount = Integer.parseInt(wechatPayCallbackParams.get("amount"));
        poiAccountDao.updatePoiBiddingBalance(amount,rd.getPoiId());


        rd.setBalance(accountData.getCurBiddingBalance() + amount);
        rd.setTimeEnd(wechatPayCallbackParams.get("timeEnd"));
        biddingDao.updateRechargeRd(rd);

        logger.info("竞价充值成功!rd={},充值额={}分,充值前余额={}",rd,amount,accountData.getCurBiddingBalance());

        return BizPacket.success();
    }
}