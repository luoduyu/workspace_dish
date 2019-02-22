package com.wmt.wechat.service.balance;

import com.alibaba.fastjson.JSONObject;
import com.wmt.commons.constants.RedisConstants;
import com.wmt.commons.domain.id.Generator;
import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.commons.enums.CostCate;
import com.wmt.commons.enums.PayStatus;
import com.wmt.commons.enums.PayWay;
import com.wmt.commons.util.DateTimeUtil;
import com.wmt.dlock.lock.DLock;
import com.wmt.wechat.common.Constants;
import com.wmt.wechat.dao.balance.BalanceDao;
import com.wmt.wechat.dao.order.OrderDao;
import com.wmt.wechat.dao.poi.PoiAccountDao;
import com.wmt.wechat.dao.poi.PoiDao;
import com.wmt.wechat.domain.util.WechatUtil;
import com.wmt.wechat.model.balance.BalanceConsumeRd;
import com.wmt.wechat.model.balance.BalanceRechargeRD;
import com.wmt.wechat.model.balance.BalanceSettingData;
import com.wmt.wechat.model.balance.CurrencyStageData;
import com.wmt.wechat.model.order.OrderData;
import com.wmt.wechat.model.poi.PoiAccountData;
import com.wmt.wechat.model.poi.PoiData;
import com.wmt.wechat.model.poi.PoiMemberRDData;
import com.wmt.wechat.model.poi.PoiUserData;
import com.wmt.wechat.service.pay.PayWechatService;
import com.wmt.wechat.service.pay.util.MD5Util;
import com.wmt.wechat.service.pay.util.Sha1Util;
import com.wmt.wechat.service.pay.util.WechatXMLParser;
import com.wmt.wechat.service.poi.PoiMemberService;
import com.wmt.wechat.service.redis.RedisService;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("balanceService")
public class BalanceServiceImpl implements  BalanceService {

    private static Logger logger = LoggerFactory.getLogger(BalanceServiceImpl.class);

    private @Value("${devMode}") boolean devMode;
    private @Resource
    BalanceDao balanceDao;
    private @Resource
    PoiDao poiDao;
    private @Resource PoiMemberService poiMemberService;
    private @Resource
    PoiAccountDao poiAccountDao;
    private @Resource
    PayWechatService payWechatService;
    private @Resource
    OrderDao orderDao;
    private @Resource
    RedisService redisService;
    private @Resource StringRedisTemplate stringRedisTemplate;

    private static Long ACQUIRE_TIMEOUT_IN_MILLIS = (long) Integer.MAX_VALUE;

    @Override
    public List<CurrencyStageData> getStageDataList() {

        // XXX 考虑基于redis存储
        return balanceDao.getStageDataList();
    }


    private static final String RECHARGE_ID = "rdId=";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BizPacket recharge(PoiUserData userData, int amount)throws Exception {
        BalanceRechargeRD rd = createRechargeRd(userData,PayWay.WECHAT,amount,-1,-1);
        balanceDao.addRechargeRd(rd);

        String nonce_str = Sha1Util.getNonceStr();
        String body = "外卖通-余额充值";
        String attach = RECHARGE_ID +rd.getId();

        BizPacket bizPacket = payWechatService.prePayOrder(userData.getOpenid(),nonce_str,body,attach,rd.getOrderId(),amount, Constants.PAY_CALLBACK_URL_ALL_BALANCE);
        logger.info("余额充值--统一下单结果={}",bizPacket.toString());

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

        logger.info("用户[{}]余额充值,充值记录={},nonce_str={},prepay_id={}",userData,rd,nonce_str,bizPacket.getData().toString());
        return BizPacket.success(prePayParams);
    }


    private String buildBody(int amount){
        return "poi-balance-"+amount+"fen";
    }

    /**
     * 充值rd
     * @param userData
     * @param amount
     * @param totalBalance
     * @return
     */
    private BalanceRechargeRD createRechargeRd(PoiUserData userData, PayWay payWay, int amount, int totalBalance, int totalRedBalance){
        BalanceRechargeRD rd = new BalanceRechargeRD();
        rd.setAmount(amount);
        rd.setBalance(totalBalance);
        rd.setRedBalance(totalRedBalance);
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
    public BizPacket rechargeCallback(Map<String,String> wechatPayCallbackParams) throws InterruptedException {
        String attch = wechatPayCallbackParams.get("attach");
        if(StringUtils.isEmpty(attch)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"余额充值回调中缺少attch(rechargeId):null");
        }

        String rdId= attch.replace(RECHARGE_ID,"");
        BalanceRechargeRD rd = balanceDao.getRechargeData(rdId);
        if(rd == null) {
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "根据rechargeId未能找到充值记录!attch(rdId)=" + rdId);
        }

        String orderId = wechatPayCallbackParams.get("orderId");
        if(StringUtils.isEmpty(orderId)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "余额充值回调中缺少orderId="+orderId);
        }
        if(!rd.getOrderId().equals(orderId)){
            return BizPacket.error(HttpStatus.CONFLICT.value(), "余额充值回调中的参数冲突(分别代表了不同的订单)!orderId="+orderId+",rdId="+rdId);
        }


        int oldPayStatus = rd.getPayStatus();
        rd.setPayStatus(PayStatus.PAIED.value());
        rd.setTransactionId(wechatPayCallbackParams.get("transactionId"));

        String summary = WechatXMLParser.joinSummary(wechatPayCallbackParams);
        rd.setSummary(summary);



        // 店铺帐户锁
        DLock poiDLock = new DLock(stringRedisTemplate, RedisConstants.CANSHU_POI+rd.getPoiId());

        PoiAccountData accountData = null;
        int amount = Integer.parseInt(wechatPayCallbackParams.get("amount"));

        // 返赠额计算
        int red = getRedReward(amount);
        try {
            accountData = poiAccountDao.getAccountData(rd.getPoiId());;
            poiDLock.acquire(ACQUIRE_TIMEOUT_IN_MILLIS);
            poiAccountDao.updatePoiBalance(accountData.getCurBalance()+amount,accountData.getCurRedBalance()+red,rd.getPoiId());
        } finally {
            poiDLock.release();
        }

        rd.setBalance(accountData.getCurBalance() + amount);
        rd.setRedBalance(accountData.getCurRedBalance() + red);
        rd.setTimeEnd(wechatPayCallbackParams.get("timeEnd"));
        balanceDao.updateRechargeRd(rd,oldPayStatus);
        logger.info("余额充值成功!rd={},充值额={}分,充值前余额={},充值前红包余额={}",rd,amount,accountData.getCurBalance(),accountData.getCurRedBalance());

        return BizPacket.success();
    }

    /**
     * 计算返赠金额
     * @param amount 充值金额,单位:分
     * @return
     */
    private int getRedReward(int amount){

        // 开发模式下一律返回1分钱
        if(devMode){
            return 1;
        }

        BalanceSettingData settingData = redisService.getBalanceSetting();

        // 若配置不存在,固定为10%,且充值金额必须大于等于100元(10000分)
        if(settingData == null){
            if(amount < 10000){
                return 0;
            }

            int red = WechatUtil.roundDown(WechatUtil.mul4Float(amount,0.1f));
            return red;
        }

        // 若存在设置时

        // 低于充值下限,无返现
        if(amount < settingData.getRedRewardLowerLimit()){
            return 0;
        }

        float percent = WechatUtil.div4Float(settingData.getRedRewardPercent(),10000);
        int red = WechatUtil.roundDown(WechatUtil.mul4Float(amount,percent));
        return red;
    }

    @Override
    public BizPacket getMyRechargeData(PoiUserData userData, int index, int pageSize) {
        try {
            PoiData poiData = poiDao.getPoiData(userData.getPoiId());
            if(poiData == null){
                return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
            }


            int total = balanceDao.countRechargeData(poiData.getId());
            if(total <= 0){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"您还没有充过值!");
            }
            List<BalanceRechargeRD> list = balanceDao.getRechargeDataList(poiData.getId(),index * pageSize,pageSize);

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


            int total = balanceDao.countConsumeData(poiData.getId());
            if(total <= 0){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"暂时还没有消费记录!");
            }
            List<BalanceConsumeRd> list = balanceDao.getConsumeDataList(poiData.getId(),index * pageSize,pageSize);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total",total);
            jsonObject.put("list",list);
            return BizPacket.success(jsonObject);
        } catch (Exception e) {
            logger.error("usr="+userData+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BizPacket memberBuy(PoiUserData userData, PoiMemberRDData rd) throws Exception {
        PoiAccountData accountData = poiAccountDao.getAccountData(rd.getPoiId());
        if(rd.getPayment()>0){
            int total = accountData.getCurBalance()+accountData.getCurRedBalance();
            if(total < (rd.getTotal()-rd.getDiscount()) ){
                return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(),"余额不足!");
            }
        }


        // 从红包帐户中扣减额度
        int takeout_red = 0;

        // 从红余额中扣减额度
        int takeout_balance  = 0;

        int payment = rd.getPayment();

        // 店铺帐户锁
        DLock poiDLock = new DLock(stringRedisTemplate, RedisConstants.CANSHU_POI+rd.getPoiId());
        try {
            poiDLock.acquire(ACQUIRE_TIMEOUT_IN_MILLIS);

            if (payment > 0) {
                if (accountData.getCurRedBalance() <= payment) {
                    // 优先扣减红包帐户
                    takeout_red = accountData.getCurRedBalance();

                    // 余额帐户应扣款 = 总款 - 折扣款- 红包抵扣款
                    takeout_balance = payment - accountData.getCurRedBalance();
                    rd.setPayment(takeout_balance);
                    accountData.setCurRedBalance(0);

                    // 再从余额帐户中扣除
                    accountData.setCurBalance(accountData.getCurBalance() - takeout_balance);
                } else {
                    takeout_red = payment;
                    takeout_balance = 0;
                    rd.setPayment(0);

                    // 红包帐户就够了
                    accountData.setCurRedBalance(accountData.getCurRedBalance() - takeout_red);
                }

                // 扣款持久化
                poiAccountDao.updatePoiBalance(accountData.getCurBalance(), accountData.getCurRedBalance(), accountData.getPoiId());
            }

            // 填写消费记录
            BalanceConsumeRd consumeRd = createBalanceConsumeRd(userData, rd.getOrderId(), takeout_red, takeout_balance, CostCate.MEMBER_BUY);

            // 更新购买记录
            rd.setPayWay(PayWay.BALANCE.value());
            rd.setSummary(consumeRd.getSummary());
            rd.setTransactionId(String.valueOf(consumeRd.getId()));

            BizPacket ret = poiMemberService.onMemberBoughtSucc(rd, consumeRd.getCreateTime(), userData.getId());
            return ret;

        } finally {
            poiDLock.release();
        }
    }

    private BalanceConsumeRd createBalanceConsumeRd (PoiUserData userData,String orderId,int takeout_red,int takeout_balance,CostCate costCate){
        BalanceConsumeRd rd = new BalanceConsumeRd();
        rd.setCreateTime(DateTimeUtil.now());
        rd.setOrderId(orderId);
        rd.setPoiId(userData.getPoiId());
        rd.setUserId(userData.getId());
        rd.setUserName(userData.getName());
        rd.setCateId(costCate.ordinal());

        rd.setAmount(takeout_red + takeout_balance);
        StringBuilder summary = new StringBuilder();
        summary.append("红包帐户扣减:").append(takeout_red).append(",余额扣减:").append(takeout_balance);
        rd.setSummary(summary.toString());

        balanceDao.addConsumeRd(rd);
        return rd;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BizPacket onOrderPayConfirm(PoiUserData userData, OrderData rd) throws Exception {
        PoiAccountData accountData = poiAccountDao.getAccountData(rd.getPoiId());
        int total = accountData.getCurBalance() + accountData.getCurRedBalance();
        if(rd.getPayment() >0){
            if (total < rd.getPayment()) {
                return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(), "余额不足!");
            }
        }


        // 从红包帐户中扣减额度
        int takeout_red = 0;

        // 从红余额中扣减额度
        int takeout_balance = 0;


        // 店铺帐户锁
        DLock poiDLock = new DLock(stringRedisTemplate, RedisConstants.CANSHU_POI+rd.getPoiId());
        try {
            poiDLock.acquire(ACQUIRE_TIMEOUT_IN_MILLIS);
            if (rd.getPayment() > 0) {

                if (accountData.getCurRedBalance() <= rd.getPayment()) {
                    // 优先扣减红包帐户
                    takeout_red = accountData.getCurRedBalance();

                    // 余额帐户应扣款 = 总款 - 折扣款- 红包抵扣款
                    takeout_balance = rd.getPayment() - accountData.getCurRedBalance();
                    accountData.setCurRedBalance(0);

                    // 再从余额帐户中扣除
                    accountData.setCurBalance(accountData.getCurBalance() - takeout_balance);
                } else {
                    takeout_red = rd.getTotal();
                    takeout_balance = 0;

                    // 红包帐户就够了
                    accountData.setCurRedBalance(accountData.getCurRedBalance() - takeout_red);
                }

                // 扣款持久化
                poiAccountDao.updatePoiBalance(accountData.getCurBalance(), accountData.getCurRedBalance(), accountData.getPoiId());
            }
        } finally {
            poiDLock.release();
        }

        // 填写消费记录
        BalanceConsumeRd consumeRd = createBalanceConsumeRd(userData, rd.getOrderId(), takeout_red, takeout_balance, CostCate.ORDER_PAY);

        // 更新购买记录
        int oldPayStatus = rd.getPayStatus();
        rd.setPayWay(PayWay.BALANCE.value());
        rd.setSummary(consumeRd.getSummary());
        rd.setTimeEnd(DateTimeUtil.now());
        rd.setPayStatus(PayStatus.PAIED.value());
        rd.setTransactionId(String.valueOf(consumeRd.getId()));
        rd.setPayUserId(userData.getId());
        orderDao.updateOrderData(rd, oldPayStatus);

        return BizPacket.success();
    }
}