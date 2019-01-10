package com.amt.wechat.service.poi.impl;

import com.alibaba.fastjson.JSONObject;
import com.amt.wechat.dao.dish.DishDao;
import com.amt.wechat.dao.member.MemberDao;
import com.amt.wechat.dao.poi.PoiDao;
import com.amt.wechat.dao.poi.PoiMaterialDao;
import com.amt.wechat.domain.id.Generator;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.DateTimeUtil;
import com.amt.wechat.form.basic.BasicSettingForm;
import com.amt.wechat.form.poi.MyMemberDataForm;
import com.amt.wechat.model.member.MemberCardData;
import com.amt.wechat.model.poi.*;
import com.amt.wechat.service.poi.PoiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;


@Service("poiService")
public class PoiServiceImpl implements PoiService {
    private static Logger logger = LoggerFactory.getLogger(PoiServiceImpl.class);

    private @Resource PoiMaterialDao poiMaterialDao;
    private @Resource PoiDao poiDao;
    private @Resource DishDao dishDao;
    private @Resource MemberDao memberDao;


    @Override
    public List<MaterialData> getPoiMaterialDataList() {
        return poiMaterialDao.getPoiMaterialDataList();
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


    //poiDao.addPoiData(poiData);

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

            boolean newbie = memberNewbie(userData.getPoiId());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("newbie",newbie);
            if(!newbie){
                return BizPacket.success(jsonObject);
            }

            PoiAccountData accountData =  poiDao.getAccountData(userData.getPoiId());

            // 已节省的花费
            MyMemberDataForm myMemberDataForm = new MyMemberDataForm();
            if(accountData != null){
                myMemberDataForm.setCostSave(accountData.getCostSave());
            }

            // 会员数据
            PoiMemberData memberData = poiDao.getPoiMemberData(userData.getPoiId());
            myMemberDataForm = let(myMemberDataForm,memberData);
            jsonObject.put("poiMemberData",myMemberDataForm);

            return BizPacket.success(jsonObject);
        } catch (Exception e) {
            logger.error("user="+userData+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    private MyMemberDataForm let(MyMemberDataForm myMemberDataForm,PoiMemberData memberData){
        if(memberData == null){
           return myMemberDataForm;
        }

        myMemberDataForm.setAutoFee(memberData.getAutoFee());
        myMemberDataForm.setAutoFeeRenew(memberData.getAutoFeeRenew());
        myMemberDataForm.setBuyTime(memberData.getBuyTime());
        myMemberDataForm.setDurationUnit(memberData.getDurationUnit());
        myMemberDataForm.setExpiredAt(memberData.getExpiredAt());

        return myMemberDataForm;
    }

    /**
     * 是否会员新手
     * @param poiId
     * @return
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
        return BizPacket.success(list);
    }

    @Override
    public BizPacket memberBuy(PoiUserData userData, int memberCardId, int feeRenew) {
        MemberCardData data = memberDao.getMemberCardData(memberCardId);
        if(data == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"非法的会员卡Id");
        }

        PoiMemberData memberData = poiDao.getPoiMemberData(userData.getPoiId());


        //
        return BizPacket.success();
    }
}