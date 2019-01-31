package com.amt.wechat.service.poi.impl;

import com.amt.wechat.dao.dish.DishDao;
import com.amt.wechat.dao.poi.PoiAccountDao;
import com.amt.wechat.dao.poi.PoiDao;
import com.amt.wechat.dao.poi.PoiUserDao;
import com.amt.wechat.domain.id.Generator;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.DateTimeUtil;
import com.amt.wechat.form.basic.BasicSettingForm;
import com.amt.wechat.model.poi.PoiAccountData;
import com.amt.wechat.model.poi.PoiData;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.service.poi.EmplIdentity;
import com.amt.wechat.service.poi.PoiService;
import com.amt.wechat.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;


@Service("poiService")
public class PoiServiceImpl implements PoiService {
    private static Logger logger = LoggerFactory.getLogger(PoiServiceImpl.class);

    private @Value("${devMode}") boolean devMode;

    private @Resource PoiDao poiDao;
    private @Resource DishDao dishDao;
    private @Resource PoiAccountDao poiAccountDao;
    private @Resource PoiUserDao poiUserDao;
    private @Resource RedisService redisService;



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



    @Override
    public BizPacket balancePwdSet(PoiUserData userData,String pwd){
        try {
            PoiData poiData = poiDao.getPoiData(userData.getPoiId());
            if(poiData == null){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"店铺已不存!");
            }
            if(!StringUtils.isEmpty(poiData.getBalancePwd())){
                return BizPacket.error(HttpStatus.FORBIDDEN.value(),"密码已设置!");
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
    public BizPacket balancePwdRequired(PoiUserData userData, int flag) {
        try {
            PoiData poiData = poiDao.getPoiData(userData.getPoiId());
            if(poiData == null){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"店铺已不存!");
            }

            if(poiData.getBalancePwdFree() == flag){
                return BizPacket.success();
            }
            poiData.setBalancePwdFree(flag);
            poiDao.updateBalancePwdRequired(flag,poiData.getId());
            return BizPacket.success();
        } catch (Exception e) {
            logger.error("userData="+userData+",flag="+flag+",e="+e.getMessage(),e);
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
    public BizPacket forgetReset(PoiUserData userData,String newPwd){
        PoiData poiData = poiDao.getPoiData(userData.getPoiId());
        if(poiData == null) {
            return BizPacket.error(HttpStatus.FORBIDDEN.value(), "你没有店铺!");
        }
        poiData.setBalancePwd(newPwd.trim());
        poiDao.updateBalancePwd(poiData.getBalancePwd(),poiData.getId());
        return BizPacket.success();
    }

    @Override
    public BizPacket eleAuth(PoiUserData userData,String accountName,String accountPwd){
        try {
            if(!StringUtils.isEmpty(userData.getPoiId())){

                // 店铺存在时:
                PoiData poiData = poiDao.getPoiData(userData.getPoiId());
                if(poiData == null) {
                    return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "店铺实体信息不存在!");
                }
                if(!userData.getPoiId().equalsIgnoreCase(poiData.getId())){
                    return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "你不是本店成员!");
                }

                if(!StringUtils.isEmpty(poiData.getEleShopId())){
                    return BizPacket.error(HttpStatus.NOT_MODIFIED.value(),"已经认证过了!");
                }
                poiData.setEleShopId("eleId-temp");
                poiData.setAccountName(accountName);
                poiData.setAccountPassword(accountPwd);
                poiData.setUpdTime(DateTimeUtil.now());
                poiDao.eleAuth(poiData);

                String masterMobile = poiUserDao.getMasterMobile(poiData.getId(), EmplIdentity.MASTER.value());
                PoiAccountData accountData =  poiAccountDao.getAccountData(poiData.getId());
                return BizPacket.success(PoiData.createFrom(poiData,accountData,masterMobile));
            }

            // 店铺不存在时:
            PoiData poiData = createPoiData(userData,accountName,accountPwd);
            poiData.setEleShopId("eleId-temp");
            poiDao.addPoiData(poiData);

            PoiAccountData accountData =  createPoiAccount(poiData);
            poiAccountDao.addPoiAccountData(accountData);

            onPOICreated(userData,poiData);
            String masterMobile = poiUserDao.getMasterMobile(poiData.getId(),EmplIdentity.MASTER.value());
            return BizPacket.success(PoiData.createFrom(poiData,accountData,masterMobile));
        } catch (Exception e) {
            logger.error("userData="+userData+",accountName="+accountName+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }



    @Override
    public BizPacket mtAuth(PoiUserData userData,String accountName,String accountPwd){
        try {
            if(!StringUtils.isEmpty(userData.getPoiId())){

                // 店铺存在时:
                PoiData poiData = poiDao.getPoiData(userData.getPoiId());
                if(poiData == null) {
                    return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "店铺实体信息不存在!");
                }
                if(!userData.getPoiId().equalsIgnoreCase(poiData.getId())){
                    return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "你不是本店成员!");
                }

                if(!StringUtils.isEmpty(poiData.getEleShopId())){
                    return BizPacket.error(HttpStatus.NOT_MODIFIED.value(),"已经认证过了!");
                }
                poiData.setMtAppAuthToken("mtShopId-temp");
                poiData.setAccountName(accountName);
                poiData.setAccountPassword(accountPwd);
                poiData.setUpdTime(DateTimeUtil.now());
                poiDao.mtAuth(poiData);

                String masterMobile = poiUserDao.getMasterMobile(poiData.getId(),EmplIdentity.MASTER.value());
                PoiAccountData accountData =  poiAccountDao.getAccountData(poiData.getId());
                return BizPacket.success(PoiData.createFrom(poiData,accountData,masterMobile));
            }

            // 店铺不存在时:
            PoiData poiData = createPoiData(userData,accountName,accountPwd);
            poiData.setMtAppAuthToken("mtShopId-temp");
            poiDao.addPoiData(poiData);

            PoiAccountData accountData =  createPoiAccount(poiData);
            poiAccountDao.addPoiAccountData(accountData);

            onPOICreated(userData,poiData);
            String masterMobile = poiUserDao.getMasterMobile(poiData.getId(),EmplIdentity.MASTER.value());
            return BizPacket.success(PoiData.createFrom(poiData,accountData,masterMobile));
        } catch (Exception e) {
            logger.error("userData="+userData+",accountName="+accountName+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    /**
     * 店铺创建成功
     * @param userData
     * @param poiData
     */
    private void onPOICreated(PoiUserData userData,PoiData poiData) throws IOException {
        userData.setPoiId(poiData.getId());
        userData.setUpdTime(DateTimeUtil.now());
        poiUserDao.updateUserPoiId(poiData.getId(),userData.getUpdTime(),userData.getId());
        redisService.addPoiUser(userData);
    }


    private PoiAccountData createPoiAccount(PoiData poiData){
        PoiAccountData accountData = new PoiAccountData();

        accountData.setCurrShareBalance(0);
        accountData.setCurRedBalance(0);
        accountData.setCurBiddingBalance(0);
        accountData.setCostSave(0);
        accountData.setCurBalance(0);
        accountData.setPoiId(poiData.getId());

        return accountData;
    }

    /**
     * 创建一个缺省的店铺数据
     * @return
     */
    private PoiData createPoiData(PoiUserData userData,String accountName,String accountPwd){
        PoiData poiData = new PoiData();
        poiData.setId(Generator.uuid());
        poiData.setName(userData.getName()+"的店铺");
        poiData.setCountry("中国");
        poiData.setProvince(userData.getProvince());
        poiData.setCity(userData.getCity());
        poiData.setStreet(userData.getCity());
        poiData.setAddress(userData.getCity());
        poiData.setDistricts("");

        poiData.setBrandName("");
        poiData.setCateId(-1);
        poiData.setLogoImg("");

        poiData.setAccountName(accountName);
        poiData.setAccountPassword(accountPwd);
        poiData.setBalancePwd("");
        poiData.setBalancePwdFree(1);
        poiData.setEleShopId("");
        poiData.setMtAppAuthToken("");

        poiData.setCreateTime(DateTimeUtil.now());
        poiData.setUpdTime(poiData.getUpdTime());
        return poiData;
    }
}