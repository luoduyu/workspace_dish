package com.wmt.wechat.service.poi.impl;

import com.alibaba.fastjson.JSONObject;
import com.wmt.commons.domain.id.Generator;
import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.commons.enums.DurationUnit;
import com.wmt.commons.enums.PayStatus;
import com.wmt.commons.enums.PayWay;
import com.wmt.commons.util.DateTimeUtil;
import com.wmt.wechat.common.Constants;
import com.wmt.wechat.dao.globalsetting.GlobalSettingDao;
import com.wmt.wechat.dao.member.MemberDao;
import com.wmt.wechat.dao.member.PoiMemberDao;
import com.wmt.wechat.dao.poi.PoiAccountDao;
import com.wmt.wechat.dao.poi.PoiUserDao;
import com.wmt.wechat.domain.member.PoiMember;
import com.wmt.wechat.form.poi.MyMemberDataForm;
import com.wmt.wechat.model.member.MemberCardData;
import com.wmt.wechat.model.member.MemberFeedbackData;
import com.wmt.wechat.model.poi.PoiAccountData;
import com.wmt.wechat.model.poi.PoiMemberData;
import com.wmt.wechat.model.poi.PoiMemberRDData;
import com.wmt.wechat.model.poi.PoiUserData;
import com.wmt.wechat.service.balance.BalanceService;
import com.wmt.wechat.service.pay.PayWechatService;
import com.wmt.wechat.service.pay.util.MD5Util;
import com.wmt.wechat.service.pay.util.Sha1Util;
import com.wmt.wechat.service.pay.util.WechatXMLParser;
import com.wmt.wechat.service.poi.PoiMemberService;
import com.wmt.wechat.service.poi.PoiUserService;
import com.wmt.wechat.service.redis.RedisService;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("poiMemberService")
public class PoiMemberServiceImpl implements PoiMemberService {
    private static Logger logger = LoggerFactory.getLogger(PoiMemberServiceImpl.class);
    private @Resource
    PoiMemberDao poiMemberDao;
    private @Resource
    PoiAccountDao poiAccountDao;
    private @Resource
    MemberDao memberDao;
    private @Resource
    PoiUserService poiUserService;
    private @Resource
    GlobalSettingDao globalSettingDao;
    private @Resource
    PayWechatService payWechatService;
    private @Resource
    BalanceService balanceService;
    private @Resource
    PoiUserDao poiUserDao;
    private @Resource
    RedisService redisService;

    @Override
    public BizPacket memberDataFetch(PoiUserData userData){
        try {

            // 是否新手
            boolean newbie = memberNewbie(userData.getPoiId());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("newbie",newbie);

            // 会员数据
            PoiMemberData memberData = poiMemberDao.getPoiMemberData(userData.getPoiId());
            boolean isMember = PoiMember.isMember(memberData);
            jsonObject.put("isMember",isMember);

            if(isMember){
                // 已节省的花费
                PoiAccountData accountData =  poiAccountDao.getAccountData(userData.getPoiId());
                MyMemberDataForm myMemberDataForm = new MyMemberDataForm();
                if(accountData != null){
                    myMemberDataForm.setCostSave(accountData.getCostSave());
                }else{
                    myMemberDataForm.setCostSave(0);
                }

                // 会员数据
                myMemberDataForm = let(myMemberDataForm,memberData);
                jsonObject.put("poiMemberData",myMemberDataForm);
            }

            // 已续费的商户数
            Integer hasBeenFeeNum = globalSettingDao.getMemberNum4HasbeenFee();
            jsonObject.put("hasBeenFeeNum",hasBeenFeeNum==null?0:hasBeenFeeNum.intValue());

            return BizPacket.success(jsonObject);
        } catch (Exception e) {
            logger.error("user="+userData+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }


    /**
     * 赋值
     * @param myMemberDataForm
     * @param memberData
     * @return
     */
    private MyMemberDataForm let(MyMemberDataForm myMemberDataForm,PoiMemberData memberData){
        if(memberData == null){
            return myMemberDataForm;
        }

        myMemberDataForm.setMemberId(memberData.getId());
        myMemberDataForm.setAutoFee(memberData.getAutoFee());
        myMemberDataForm.setAutoFeeRenew(memberData.getAutoFeeRenew());
        myMemberDataForm.setBuyTime(memberData.getBuyTime());
        myMemberDataForm.setDurationUnit(memberData.getDurationUnit());
        myMemberDataForm.setDuration(memberData.getDuration());
        myMemberDataForm.setExpiredAt(memberData.getExpiredAt());

        return myMemberDataForm;
    }

    @Override
    public boolean memberNewbie(String poiId){
        int rdSize = poiMemberDao.countPoiMemberRDWithPaystatus(poiId, PayStatus.PAIED.value());
        return rdSize <= 0;
    }

    @Override
    public boolean isMember(String poiId){
        PoiMemberData memberData =  poiMemberDao.getPoiMemberData(poiId);
        return PoiMember.isMember(memberData);
    }



    @Override
    public BizPacket memberBoughtRD(PoiUserData userData,String orderId){
        PoiMemberRDData rd =  poiMemberDao.getMemberBoughtRDByOrderId(orderId);
        if(rd == null){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"数据不存在!");
        }
        if(userData.getPoiId().equalsIgnoreCase(rd.getPoiId())){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"非法访问!");
        }
        return BizPacket.success(rd);
    }

    @Override
    public BizPacket memberBoughtRDList(PoiUserData userData,int index,int pageSize){
        List<PoiMemberRDData> list =  poiMemberDao.getMemberBoughtList(userData.getPoiId(),index*pageSize,pageSize);

        if(list == null || list.isEmpty()){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"您还没有购买过会员卡1");
        }
        int total = poiMemberDao.countPoiMemberRD(userData.getPoiId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",total);
        jsonObject.put("list",list);

        return BizPacket.success(jsonObject);
    }


    /**
     * 新添加会员数据
     *
     * @param rdData
     * @return
     */
    private PoiMemberData newMember(PoiMemberRDData rdData){
        PoiMemberData memberData= new PoiMemberData();
        memberData.setAutoFee(rdData.getTotal());
        memberData.setAutoFeeRenew(rdData.getFeeRenew());
        memberData.setBuyTime(DateTimeUtil.now());
        memberData.setDurationUnit(rdData.getDurationUnit());
        memberData.setDuration(rdData.getDuration());
        memberData.setPoiId(rdData.getPoiId());

        String expiredAt = calculateExpiredDate(rdData);
        memberData.setExpiredAt(expiredAt);
        poiMemberDao.addPoiMemberData(memberData);
        return memberData;
    }

    /**
     * 新添加会员-缺省数值
     * @param poiId
     * @param feeRenew
     * @return
     */
    private PoiMemberData newMemberDefault(String poiId,int feeRenew){
        PoiMemberData memberData= new PoiMemberData();
        memberData.setAutoFee(0);
        memberData.setAutoFeeRenew(feeRenew);
        memberData.setBuyTime(DateTimeUtil.now());
        memberData.setDurationUnit("");
        memberData.setDuration(0);
        memberData.setPoiId(poiId);
        memberData.setExpiredAt("");
        poiMemberDao.addPoiMemberData(memberData);
        return memberData;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public BizPacket onMemberBoughtSucc(PoiMemberRDData rd, String endTime, String payUserId){
        boolean newbie = memberNewbie(rd.getPoiId());
        memberProcess(rd,endTime);

        //  给邀请人发奖
        String inviterId = getInviterId(payUserId);
        logger.info("准备给邀请人发放奖励!是否首次购买={},邀请人userId={}",newbie,inviterId);

        // 必须是第一次购买会员(此逻辑不抛异常)
        if(inviterId != null && inviterId.trim().length() >= 1 && newbie){
            poiUserService.onInviteSucc(inviterId,payUserId,rd);
        }
        return BizPacket.success();
    }

    private void memberProcess(PoiMemberRDData rd,String endTime){
        // 更新购买记录
        int oldPayStatus = rd.getPayStatus();
        rd.setPayStatus(PayStatus.PAIED.value());
        rd.setTimeEnd(endTime);
        poiMemberDao.updateMemberBoughtRD(rd,oldPayStatus);

        PoiMemberData memberData = poiMemberDao.getPoiMemberData(rd.getPoiId());

        // 首次购买会员
        if(memberData == null){
            newMember(rd);
            return;
        }

        // 续期
        if(memberData.getDurationUnit().equalsIgnoreCase(rd.getDurationUnit())){
            memberRenewal(memberData,rd);
            return;
        }

        // 升级会员
        memberUpgrade(memberData,rd);
    }


    private String getInviterId(String userId){
        PoiUserData userData = redisService.getPoiUserById(userId);
        if(userData != null){
            return userData.getInviterId();
        }
        return poiUserDao.getInviterId(userId);
    }

    /**
     * 会员升级
     * @param memberData
     * @param rdData
     */
    private void memberUpgrade(PoiMemberData memberData,PoiMemberRDData rdData){
        if(!isUpgrade(memberData,rdData)){
            return;
        }

        memberData.setAutoFee(rdData.getTotal());
        memberData.setAutoFeeRenew(rdData.getFeeRenew());
        memberData.setBuyTime(DateTimeUtil.now());
        memberData.setDurationUnit(rdData.getDurationUnit());
        memberData.setDuration(rdData.getDuration());
        memberData.setPoiId(rdData.getPoiId());


        String expiredAt = calculateExpiredDate(LocalDateTime.now(),rdData);
        memberData.setExpiredAt(expiredAt);
        poiMemberDao.updatePoiMemberData(memberData);
    }

    private boolean isUpgrade(PoiMemberData memberData,PoiMemberRDData rdData){
        DurationUnit unit = DurationUnit.value(memberData.getDurationUnit());
        return unit.allowUpgrade(rdData.getDurationUnit());
    }



    /**
     * 会员续期
     * @param memberData
     * @param rdData
     * @return
     */
    private PoiMemberData memberRenewal(PoiMemberData memberData,PoiMemberRDData rdData){
        memberData.setAutoFee(rdData.getTotal());
        memberData.setAutoFeeRenew(rdData.getFeeRenew());
        memberData.setBuyTime(DateTimeUtil.now());

        // 最后一次购买的是什么'卡'，那就是什么卡
        memberData.setDurationUnit(rdData.getDurationUnit());
        memberData.setDuration(rdData.getDuration());
        memberData.setPoiId(rdData.getPoiId());


        LocalDateTime begin = DateTimeUtil.getDateTime(memberData.getExpiredAt());
        String expiredAt = calculateExpiredDate(begin,rdData);
        memberData.setExpiredAt(expiredAt);
        poiMemberDao.updatePoiMemberData(memberData);
        return memberData;
    }


    /**
     * 计算失效日期时间
     * @param rdData
     * @return
     */
    private String calculateExpiredDate(PoiMemberRDData rdData){
        return calculateExpiredDate(LocalDateTime.now(),rdData);
    }


    /**
     *
     * @param begin 时间起点
     * @param rdData
     * @return
     */
    private String calculateExpiredDate(LocalDateTime begin,PoiMemberRDData rdData){
        switch(rdData.getDurationUnit()){
            case "DAY":
                LocalDateTime ldt = begin.plusDays(rdData.getDuration()-1).withHour(23).withMinute(59).withSecond(59);
                return ldt.format(DateTimeUtil.FRIENDLY_DATE_TIME_FORMAT);

            case "WEEK":
                ldt = begin.plusWeeks(rdData.getDuration()).withHour(23).withMinute(59).withSecond(59).minusDays(1);
                return ldt.format(DateTimeUtil.FRIENDLY_DATE_TIME_FORMAT);

            case "MONTH":
                ldt = begin.plusMonths(rdData.getDuration()).withHour(23).withMinute(59).withSecond(59).minusDays(1);
                return ldt.format(DateTimeUtil.FRIENDLY_DATE_TIME_FORMAT);

            case "YEAR":
                ldt = begin.plusYears(rdData.getDuration()).withHour(23).withMinute(59).withSecond(59).minusDays(1);
                return ldt.format(DateTimeUtil.FRIENDLY_DATE_TIME_FORMAT);
            default:
                return "";
        }
    }


    @Override
    public BizPacket memberBuyPreview(PoiUserData userData, int memberCardId, int feeRenew) {
        MemberCardData cardData = memberDao.getMemberCardData(memberCardId);
        if(cardData == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"非法的会员卡Id");
        }

        // 购买记录/摘要持久化
        // TODO 考虑一种更佳的做法,避免每次创建新记录。
        // 从两方面考虑,一方面是业务流程优化(主要考虑方向),另一方面是考虑从原有的记录里取,或者设置过期时间什么的。
        PoiMemberRDData rdData = createMemberCardBoughtRD(userData,cardData,feeRenew);
        poiMemberDao.addMemberBoughtRD(rdData);

        JSONObject jsonObject = rd2JSON(rdData);
        return BizPacket.success(jsonObject);
    }

    private JSONObject rd2JSON(PoiMemberRDData rdData){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("durationUnit",rdData.getDurationUnit());
        jsonObject.put("duration",rdData.getDuration());
        jsonObject.put("buyTime",rdData.getBuyTime());
        jsonObject.put("total",rdData.getTotal());
        jsonObject.put("discount",rdData.getDiscount());
        jsonObject.put("payment",rdData.getPayment());
        jsonObject.put("orderId",rdData.getOrderId());
        jsonObject.put("feeRenew",rdData.getFeeRenew());
        return jsonObject;
    }



    @Override
    public BizPacket memberBuyConfirm(PoiUserData userData, String orderId, PayWay payWay) throws Exception{
        PoiMemberRDData rd = poiMemberDao.getMemberBoughtRDByOrderId(orderId);
        if(rd == null){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"订单不存在!");
        }
        if(!rd.getPoiId().equalsIgnoreCase(userData.getPoiId())){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"非法请求!");
        }
        if(rd.getPayStatus() == PayStatus.PAIED.value()){
            return BizPacket.error(HttpStatus.NOT_ACCEPTABLE.value(),"订单已经支付过了!");
        }

        if(payWay == PayWay.BALANCE){
            return balanceService.memberBuy(userData,rd);
        }
        return memberBuy4WX(userData,rd);
    }


    private static final String MEMBER_BUY_ID = "rdId=";
    private static final String USER_ID = "puid=";

    /**
     * 微信帐户支付
     * @param userData
     * @param rd
     * @return
     * @throws Exception
     */
    private BizPacket memberBuy4WX(PoiUserData userData,PoiMemberRDData rd)throws Exception{
        String nonce_str = Sha1Util.getNonceStr();
        String body = "外卖通-会员购买";
        String attach = MEMBER_BUY_ID +rd.getId()+","+USER_ID+userData.getId();

        BizPacket bizPacket = payWechatService.prePayOrder(userData.getOpenid(),nonce_str,body,attach,rd.getOrderId(),rd.getPayment(), Constants.PAY_CALLBACK_URL_ALL_MEMBER_BUY);
        logger.info("会员卡购买--统一下单结果={}",bizPacket.toString());

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

        logger.info("用户[{}]购买会员,购买记录={},nonce_str={},prepay_id={}",userData,rd,nonce_str,bizPacket.getData().toString());
        return BizPacket.success(prePayParams);
    }


    private String buildBody(int amount){
        return "member-buy-"+amount+"fen";
    }


    /**
     * 写入购买日志
     * @param userData
     * @param cardData
     * @param feeRenew
     * @return
     */
    private PoiMemberRDData createMemberCardBoughtRD(PoiUserData userData, MemberCardData cardData, int feeRenew){
        PoiMemberRDData rd = new PoiMemberRDData();
        rd.setBuyTime(DateTimeUtil.now());
        rd.setDuration(cardData.getDuration());
        rd.setDurationUnit(cardData.getDurationUnit());
        rd.setFeeRenew(feeRenew);

        // 新手折扣计算
        boolean isMemberNewbie = memberNewbie(userData.getPoiId());
        int discount = isMemberNewbie? cardData.getNewDiscount():0;
        rd.setDiscount(discount);

        rd.setTotal(cardData.getPrice());;
        rd.setPoiId(userData.getPoiId());
        rd.setUserId(userData.getId());
        rd.setPayStatus(PayStatus.NOT_PAID.value());

        // 实付 = 应付总款 - 折扣
        rd.setPayment( rd.getTotal() - rd.getDiscount());

        rd.setOrderId(Generator.generate());
        rd.setPayWay(-1);
        rd.setTransactionId("");
        rd.setSummary("");
        rd.setTimeEnd("");
        logger.info("rd={},user={},mcardData={},feeRenew={},isMemberNewbie={},discount={}",rd,userData,cardData,feeRenew,isMemberNewbie,discount);
        return rd;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public BizPacket memberBuyCallback(Map<String, String> wechatPayCallbackParams) {
        String attch = wechatPayCallbackParams.get("attach");
        if(StringUtils.isEmpty(attch)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"会员购买付款回调中缺少attch(id):null");
        }

        String[] pairs = attch.split(",");
        String rdId= pairs[0].replace(MEMBER_BUY_ID,"");
        PoiMemberRDData rd = poiMemberDao.getMemberBoughtRDById(rdId);
        if(rd == null) {
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "根据rdId未能找到会员购买记录!attch(rdId)=" + rdId);
        }

        String orderId = wechatPayCallbackParams.get("orderId");
        if(StringUtils.isEmpty(orderId)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "会员购买回调中缺少orderId="+orderId);
        }
        if(!rd.getOrderId().equals(orderId)){
            return BizPacket.error(HttpStatus.CONFLICT.value(), "会员购买回调中的参数冲突(分别代表了不同的订单)!orderId="+orderId+",rdId="+rdId);
        }

        rd.setPayWay(PayWay.WECHAT.value());
        rd.setTransactionId(wechatPayCallbackParams.get("transactionId"));

        String summary = WechatXMLParser.joinSummary(wechatPayCallbackParams);
        rd.setSummary(summary);


        int payment = getPayment(rd,wechatPayCallbackParams.get("cashFee"));
        rd.setPayment(payment);
        rd.setDiscount(rd.getTotal()  - rd.getPayment());

        String userId =  pairs[1].replace(USER_ID,"");
        BizPacket ret = this.onMemberBoughtSucc(rd,wechatPayCallbackParams.get("timeEnd"), userId);
        logger.info("微信付款购买会员回调成功!rd={}",rd);
        return ret;
    }

    private int getPayment(PoiMemberRDData rd,String cashFee){
        try {
            int payment = Integer.parseInt(cashFee);
            return payment;
        } catch (NumberFormatException e) {
            logger.error("rd="+rd+",cashFee="+cashFee+",e="+e.getMessage());
            return rd.getTotal() - rd.getDiscount();
        }
    }

    @Override
    public BizPacket feeRenewSet(PoiUserData userData,int feeRenew){
        try {
            PoiMemberData memberData = poiMemberDao.getPoiMemberData(userData.getPoiId());
            if(memberData == null){
                newMemberDefault(userData.getPoiId(),feeRenew);
                return BizPacket.success();
            }

            if(memberData.getAutoFeeRenew() == feeRenew){
                return BizPacket.success();
            }
            poiMemberDao.updatePoiMemberFreeRenew(feeRenew,memberData.getPoiId());
            return BizPacket.success();
        } catch (Exception e) {
            logger.error("userData="+userData+",feeRenew="+feeRenew+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Override
    public BizPacket autoFeeRenewCencel(PoiUserData userData) {
        try {
            PoiMemberData memberData = poiMemberDao.getPoiMemberData(userData.getPoiId());
            if(memberData == null){
                return BizPacket.success();
            }

            poiMemberDao.updateMemberFeeAutoRenew(0,memberData.getPoiId());
            return BizPacket.success();
        } catch (Exception e) {
            logger.error("userData="+userData+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Override
    public BizPacket memberFeedback(PoiUserData userData, String svcQty, String suggestText) {
        try {
            MemberFeedbackData data = new MemberFeedbackData();
            data.setCreateTime(DateTimeUtil.now());
            data.setPoiId(userData.getPoiId());
            data.setStatus(0);
            data.setSuggestText(suggestText);
            data.setSvcQty(svcQty);
            data.setUserId(userData.getId());
            poiMemberDao.addFeedback(data);
            return BizPacket.success();
        } catch (Exception e) {
            logger.error("userData="+userData+",svcQty="+svcQty+",suggestText="+suggestText+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }
}
