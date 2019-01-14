package com.amt.wechat.service.poi.impl;

import com.alibaba.fastjson.JSONObject;
import com.amt.wechat.dao.dish.DishDao;
import com.amt.wechat.dao.globalsetting.GlobalSettingDao;
import com.amt.wechat.dao.member.MemberDao;
import com.amt.wechat.dao.poi.PoiDao;
import com.amt.wechat.dao.poi.PoiUserDao;
import com.amt.wechat.domain.id.Generator;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.DateTimeUtil;
import com.amt.wechat.domain.util.WechatUtil;
import com.amt.wechat.form.basic.BasicSettingForm;
import com.amt.wechat.form.poi.MyMemberDataForm;
import com.amt.wechat.model.member.MemberCardData;
import com.amt.wechat.model.poi.*;
import com.amt.wechat.service.order.PayStatus;
import com.amt.wechat.service.poi.EmplIdentity;
import com.amt.wechat.service.poi.PoiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;


@Service("poiService")
public class PoiServiceImpl implements PoiService {
    private static Logger logger = LoggerFactory.getLogger(PoiServiceImpl.class);

    private @Value("${devMode}") boolean devMode;

    private @Resource PoiDao poiDao;
    private @Resource DishDao dishDao;
    private @Resource MemberDao memberDao;
    private @Resource PoiUserDao poiUserDao;
    private @Resource GlobalSettingDao globalSettingDao;



    @Override
    public BizPacket bossInviteIn(PoiUserData bossData,String name,String mobile){
        try {
            PoiUserData employee =  poiUserDao.getPOIUserDataByMobile(mobile);
            if(employee != null && !StringUtils.isEmpty(employee.getPoiId())){
                if(!employee.getPoiId().equalsIgnoreCase(bossData.getPoiId())){
                    return BizPacket.error(HttpStatus.CONFLICT.value(),"抱歉,此店员已经挂靠在其它商户了!");
                }
                return BizPacket.success();
            }

            PoiCandidate candidate = poiUserDao.getPoiCandidate(mobile);
            if(candidate != null){
                if(!StringUtils.isEmpty(candidate.getPoiId())){
                    if(!candidate.getPoiId().equalsIgnoreCase(bossData.getPoiId())){
                        return BizPacket.error(HttpStatus.CONFLICT.value(), "抱歉,已经有其他老板向此店员发出邀请了!");
                    }
                    return BizPacket.success();
                }
                candidate.setUserId(bossData.getId());
                candidate.setCreateTime(DateTimeUtil.now());
                candidate.setMobile(mobile);
                candidate.setPoiId(bossData.getPoiId());
                poiUserDao.updateInvite(candidate);
                return BizPacket.success();
            }


            candidate = new PoiCandidate();
            candidate.setUserId(bossData.getId());
            candidate.setCreateTime(DateTimeUtil.now());
            candidate.setMobile(mobile);
            candidate.setPoiId(bossData.getPoiId());
            poiUserDao.addInvite(candidate);

            return BizPacket.success();
        } catch (Exception e) {
            logger.error("bossData="+bossData+",name="+name+",mobile="+mobile+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Override
    public BizPacket updatePoi(PoiUserData userData,BasicSettingForm form) {
        try {
            PoiData poiData = poiDao.getPoiData(userData.getPoiId());
            if(poiData == null){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"店铺不存在！");
            }

            if(!StringUtils.isEmpty(form.getPoiBrandName())){
                poiData.setBrandName(form.getPoiBrandName());
            }

            if(form.getPoiCateId() <= 0) {
                return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "非法的经营品类!cateId=" + form.getPoiCateId());
            }
            int r = dishDao.countByCateId(form.getPoiCateId());
            if(r <= 0){
                return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"非法的经营品类!cateId="+form.getPoiCateId());
            }
            poiData.setCateId(form.getPoiCateId());

            if(!StringUtils.isEmpty(form.getPoiProvince())){
                poiData.setProvince(form.getPoiProvince());
            }
            if(!StringUtils.isEmpty(form.getPoiCity())){
                poiData.setCity(form.getPoiCity());
            }
            if(!StringUtils.isEmpty(form.getPoiDistricts())){
                poiData.setDistricts(form.getPoiDistricts());
            }
            if(!StringUtils.isEmpty(form.getPoiStreet())){
                poiData.setStreet(form.getPoiStreet());
            }
            if(!StringUtils.isEmpty(form.getPoiAddress())){
                poiData.setAddress(form.getPoiAddress());
            }

            poiData.setUpdTime(DateTimeUtil.now());

            poiDao.updatePoiData(poiData);

            return BizPacket.success();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }


    /**
     * 创建一个缺省的店铺数据
     * @return
     */
    private PoiData defaultPoiData(){
        PoiData poiData = new PoiData();
        poiData.setId(Generator.uuid());
        poiData.setName("");
        poiData.setCountry("中国");
        poiData.setProvince("");
        poiData.setCity("");
        poiData.setStreet("");
        poiData.setAddress("");

        poiData.setBrandName("");
        poiData.setCateId(-1);

        poiData.setAccountName("");
        poiData.setAccountPassword("");
        poiData.setEleShopId("");
        poiData.setMtAppAuthToken("");

        poiData.setCreateTime(DateTimeUtil.now());
        poiData.setUpdTime(poiData.getUpdTime());
        return poiData;
    }


    @Override
    public BizPacket memberDataFetch(PoiUserData userData){
        try {

            // 是否新手
            boolean newbie = memberNewbie(userData.getPoiId());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("newbie",newbie);

            // 会员数据
            PoiMemberData memberData = poiDao.getPoiMemberData(userData.getPoiId());
            boolean isMember = isMember(memberData);
            jsonObject.put("isMember",isMember);

            if(isMember){
                // 已节省的花费
                PoiAccountData accountData =  poiDao.getAccountData(userData.getPoiId());
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

    /**
     * 是否会员新手
     * @param poiId
     * @return true:是新手,false:不是新手
     */
    private boolean memberNewbie(String poiId){
        int rdSize = poiDao.countPoiMemberRD(poiId);
        return rdSize <= 0;
    }

    @Override
    public boolean isMember(String poiId){
        PoiMemberData memberData =  poiDao.getPoiMemberData(poiId);
        return isMember(memberData);
    }

    @Override
    public boolean isMember(PoiMemberData memberData){
        if(memberData == null){
            return false;
        }
        try {
            int flag = DateTimeUtil.getTime(memberData.getExpiredAt()).compareTo(LocalDateTime.now());
            if(flag <= 0 ){
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("expiredAt="+memberData.getExpiredAt()+",e="+e.getMessage(),e);
            return false;
        }
    }

    @Override
    public BizPacket memberBoughtRD(PoiUserData userData,int index,int pageSize){
        List<PoiMemberRDData> list =  poiDao.getMemberBoughtList(userData.getPoiId(),index*pageSize,pageSize);

        if(list == null || list.isEmpty()){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"您还没有购买过会员卡1");
        }
        int total = poiDao.countPoiMemberRD(userData.getPoiId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",total);
        jsonObject.put("list",list);

        return BizPacket.success(jsonObject);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BizPacket memberBuy(PoiUserData userData, int memberCardId) {
        MemberCardData cardData = memberDao.getMemberCardData(memberCardId);
        if(cardData == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"非法的会员卡Id");
        }

        boolean isMemberNewbie = memberNewbie(userData.getPoiId());
        PoiMemberRDData rdData = writeMemberCardBoughtRD(userData,cardData,1,isMemberNewbie);



        // TODO 等待支付完成
        if(devMode){
            PoiMemberData memberData = poiDao.getPoiMemberData(userData.getPoiId());
            if(memberData == null){
                addMemberData(rdData,cardData);
            }else{
                updateMemberData(memberData,rdData,cardData);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("isMemberNewbie",isMemberNewbie);
            jsonObject.put("memberCardId",memberCardId);
            jsonObject.put("boughtRecord",rdData);
            jsonObject.put("expiredAt",memberData.getExpiredAt());

            return BizPacket.success(jsonObject);
        }

        /*
        PoiMemberData memberData = poiDao.getPoiMemberData(userData.getPoiId());
        if(memberData == null){

        }
        */
        return BizPacket.success();
    }


    /**
     * 更新会员信息
     * @param memberData
     * @param rdData
     * @param cardData
     * @return
     */
    private PoiMemberData updateMemberData(PoiMemberData memberData,PoiMemberRDData rdData,MemberCardData cardData){
        memberData.setAutoFee(cardData.getPrice());
        memberData.setAutoFeeRenew(rdData.getFeeRenew());
        memberData.setBuyTime(DateTimeUtil.now());
        memberData.setDurationUnit(cardData.getDurationUnit());
        memberData.setDuration(cardData.getDuration());
        memberData.setPoiId(rdData.getPoiId());

        String expiredAt = calculateExpiredDate(cardData);
        memberData.setExpiredAt(expiredAt);
        poiDao.updatePoiMemberData(memberData);
        return memberData;
    }

    /**
     * 新添加会员数据
     *
     * @param rdData
     * @param cardData
     * @return
     */
    private PoiMemberData addMemberData(PoiMemberRDData rdData,MemberCardData cardData){
        PoiMemberData memberData= new PoiMemberData();
        memberData.setAutoFee(cardData.getPrice());
        memberData.setAutoFeeRenew(rdData.getFeeRenew());
        memberData.setBuyTime(DateTimeUtil.now());
        memberData.setDurationUnit(cardData.getDurationUnit());
        memberData.setDuration(cardData.getDuration());
        memberData.setPoiId(rdData.getPoiId());

        String expiredAt = calculateExpiredDate(cardData);
        memberData.setExpiredAt(expiredAt);
        poiDao.addPoiMemberData(memberData);
        return memberData;
    }

    /**
     * 写入购买日志
     * @param userData
     * @param cardData
     * @param feeRenew
     * @param isMemberNewbie
     * @return
     */
    private PoiMemberRDData writeMemberCardBoughtRD(PoiUserData userData,MemberCardData cardData,int feeRenew,boolean isMemberNewbie){
        PoiMemberRDData rd = new PoiMemberRDData();
        rd.setBuyTime(DateTimeUtil.now());
        rd.setDuration(cardData.getDuration());
        rd.setDurationUnit(cardData.getDurationUnit());
        rd.setFeeRenew(feeRenew);
        if(isMemberNewbie){
            rd.setNewDiscount(cardData.getNewDiscount());
        }else{
            rd.setNewDiscount(0);
        }
        rd.setTotal(cardData.getPrice());;
        rd.setPoiId(userData.getPoiId());
        rd.setUserId(userData.getId());
        rd.setPayStatus(PayStatus.NOT_PAID.value());
        rd.setPayTime("");
        rd.setPayNo("");
        rd.setPayment(rd.getTotal() - rd.getNewDiscount());

        poiDao.addMemberBoughtRD(rd);
        return rd;
    }

    /**
     * 计算失效日期时间
     * @param card
     * @return
     */
    private String calculateExpiredDate(MemberCardData card){
        LocalDateTime now = LocalDateTime.now();
        switch(card.getDurationUnit()){
            case "DAY":
                LocalDateTime ldt = LocalDateTime.now().plusDays(card.getDuration()-1).withHour(23).withMinute(59).withSecond(59);
                return ldt.format(DateTimeUtil.FRIENDLY_DATE_TIME_FORMAT);

            case "WEEK":
                ldt = LocalDateTime.now().plusWeeks(card.getDuration()).withHour(23).withMinute(59).withSecond(59).minusDays(1);
                return ldt.format(DateTimeUtil.FRIENDLY_DATE_TIME_FORMAT);

            case "MONTH":
                ldt = LocalDateTime.now().plusMonths(card.getDuration()).withHour(23).withMinute(59).withSecond(59).minusDays(1);
                return ldt.format(DateTimeUtil.FRIENDLY_DATE_TIME_FORMAT);

            case "YEAR":
                ldt = LocalDateTime.now().plusYears(card.getDuration()).withHour(23).withMinute(59).withSecond(59).minusDays(1);
                return ldt.format(DateTimeUtil.FRIENDLY_DATE_TIME_FORMAT);
            default:
                return "";
        }
    }

    @Override
    public BizPacket autoFeeRenewCencel(PoiUserData userData) {
        try {
            PoiMemberData memberData = poiDao.getPoiMemberData(userData.getPoiId());
            if(memberData == null){
                return BizPacket.success();
            }

            poiDao.updateMemberFeeAutoRenew(0,memberData.getPoiId());
            return BizPacket.success();
        } catch (Exception e) {
            logger.error("userData="+userData+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Override
    public BizPacket balancePwdSet(PoiUserData userData,String pwd){
        try {
            PoiData poiData = poiDao.getPoiData(userData.getPoiId());
            if(poiData == null){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"店铺已不存!");
            }
            poiData.setBalancePwd(pwd);
            poiDao.updateBalancePwd(pwd.trim(),poiData.getId());
            return BizPacket.success();
        } catch (Exception e) {
            logger.error("userData="+userData+",pwd="+pwd+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }


    @Override
    public BizPacket balancePwdReset(PoiUserData userData, String oldPwd, String newPwd) {
        try {
            PoiData poiData = poiDao.getPoiData(userData.getPoiId());
            if(poiData == null){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"店铺已不存!");
            }

            if(!poiData.getBalancePwd().equalsIgnoreCase(oldPwd)){
                return BizPacket.error(HttpStatus.NOT_ACCEPTABLE.value(),"原密码不符!");
            }
            poiData.setBalancePwd(newPwd.trim());
            poiDao.updateBalancePwd(newPwd.trim(),poiData.getId());
            return BizPacket.success();
        } catch (Exception e) {
            logger.error("userData="+userData+",pwd="+newPwd+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Override
    public BizPacket bossTransferTo(PoiUserData boss, String userId) {
        try {
            PoiUserData userData = poiUserDao.getPOIUserDataById(userId);
            if(userData == null){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"受让人不存在,无法转让!");
            }


            BizPacket ret = WechatUtil.check(userData);
            if(ret.getCode() != HttpStatus.OK.value()){
                return  ret;
            }
            userData.setIsMaster(EmplIdentity.MASTER.value());
            poiUserDao.updatePoiUserMaster(EmplIdentity.MASTER.value(),userData.getId());
            poiUserDao.updatePoiUserMaster(EmplIdentity.EMPLOYEE.value(),boss.getId());
            return BizPacket.success();
        } catch (Exception e) {
            logger.error("boss="+boss+",userId="+userId+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Override
    public BizPacket employeeRM(PoiUserData boss,String userId){
        try {
            PoiUserData userData = poiUserDao.getPOIUserDataById(userId);
            if(userData == null){
                return BizPacket.success();
            }

            userData.setIsEnabled(0);
            userData.setPoiId("");
            poiUserDao.removePOIUser(0,"",userData.getId());
            return BizPacket.success();
        } catch (Exception e) {
            logger.error("boss="+boss+",userId="+boss+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }
}