/*
watermark参数说明：

        参数	类型	说明
        watermark	OBJECT	数据水印
        appid	String	敏感数据归属appid，开发者可校验此参数与自身appid是否一致
        timestamp	DateInt	敏感数据获取的时间戳, 开发者可以用于数据时效性校验
*/

package com.amt.wechat.service.poi.impl;

import com.alibaba.fastjson.JSONObject;
import com.amt.wechat.dao.poi.PoiAccountDao;
import com.amt.wechat.dao.poi.PoiDao;
import com.amt.wechat.dao.poi.PoiUserDao;
import com.amt.wechat.domain.id.Generator;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.DateTimeUtil;
import com.amt.wechat.domain.util.WechatUtil;
import com.amt.wechat.form.basic.WeichatLoginForm;
import com.amt.wechat.model.poi.*;
import com.amt.wechat.service.poi.EmplIdentity;
import com.amt.wechat.service.poi.PoiUserService;
import com.amt.wechat.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("poiUserService")
public class PoiUserServiceImpl implements PoiUserService {
    private static Logger logger = LoggerFactory.getLogger(PoiUserServiceImpl.class);
    private @Resource PoiUserDao poiUserDao;
    private @Resource PoiDao poiDao;
    private @Resource RedisService redisService;
    private @Resource PoiAccountDao poiAccountDao;


    @Override
    public BizPacket weichatLogin(String code, String encryptedData, String iv) {
        JSONObject sessionKeyAndOpenid = WechatUtil.getSessionKeyOrOpenId(code);
        if(sessionKeyAndOpenid == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"登录凭证校验失败!");
        }

        logger.info("用户[sessionKey_openid={},code={}]正在从微信登录!",sessionKeyAndOpenid.toString(),code);

        String sessionKey = sessionKeyAndOpenid.getString("session_key");
        String openid = sessionKeyAndOpenid.getString("openid");

        try {
            PoiUserData userData =  poiUserDao.getPOIUserDataByOpenid(openid);
            if(userData == null){
                JSONObject userJson =  WechatUtil.getUserInfo(encryptedData,sessionKey,iv);
                logger.info("创建微信用户!userJson={}",userJson);

                userData = createUser(sessionKeyAndOpenid,userJson);
                poiUserDao.addPOIUser(userData);

            }else{

                JSONObject userJson =  WechatUtil.getUserInfo(encryptedData,sessionKey,iv);
                logger.info("更新微信用户!userJson={}",userJson);
                updateUser(userData,sessionKeyAndOpenid,userJson);
                poiUserDao.updatePOIUser(userData);
            }

            redisService.addPoiUser(userData);

            //long now = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            WeichatLoginForm form = buildResponse(userData);
            return BizPacket.success(form);
        } catch (IOException e) {
            logger.error("e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }



    private PoiUserData createUser(JSONObject sessionKeyAndOpenid,JSONObject userJson){
        PoiUserData data = new PoiUserData();
        data.setPoiId("");
        data.setId(Generator.uuid());
        data.setCreateTime(DateTimeUtil.now());
        data.setUpdTime(data.getCreateTime());
        data.setOpenid(sessionKeyAndOpenid.getString("openid"));
        data.setIsAccountNonExpired(1);
        data.setIsAccountNonLocked(1);
        data.setIsCredentialsNonExpired(1);
        data.setIsEnabled(1);
        data.setIsMaster(EmplIdentity.NONE.value());
        data.setName("");
        data.setCountryCode("中国");

        data.setPassword("");
        updateUser(data,sessionKeyAndOpenid,userJson);
        return data;
    }


    private void updateUser(PoiUserData data,JSONObject sessionKeyAndOpenid,JSONObject userJson){
        data.setAccessToken(Generator.uuid());

        // unionid 不一定存在!
        String unionid = sessionKeyAndOpenid.getString("unionid");
        if(unionid != null && unionid.trim().length() != 0) {
            data.setUnionid(unionid);
        }else{
            data.setUnionid("");
        }

        if(userJson == null){
            return;
        }
        Integer gender = userJson.getInteger("gender");
        data.setGender(gender == null ?0:gender);

        String province = userJson.getString("province");
        data.setProvince(province == null?"":province);

        String city = userJson.getString("city");
        data.setCity(city == null?"":city);

        String nickName = userJson.getString("nickName");
        data.setNickName(nickName == null?"":nickName);

        String avatarUrl = userJson.getString("avatarUrl");
        data.setAvatarUrl(avatarUrl == null ?"":avatarUrl);
    }

    @Override
    public BizPacket auth4Boss(String name, String mobile, PoiUserData userData){
        try {
            PoiUserData existUser = poiUserDao.getPOIUserDataByMobileExcludeId(mobile,userData.getId());
            if(existUser != null){
                return BizPacket.error(HttpStatus.CONFLICT.value(),"抱歉,手机号'"+mobile+"'已被使用!");
            }


            // 个人资料之缓存的更新
            userData.setUpdTime(DateTimeUtil.now());
            userData.setName(name);
            userData.setMobile(mobile);
            userData.setIsMaster(EmplIdentity.MASTER.value());
            redisService.addPoiUser(userData);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()+",mobile="+mobile);
        }
        try {
            // 个人资料之持久化
            poiUserDao.update4AuthBoss(userData.getIsMaster(),name,mobile,userData.getUpdTime(),userData.getId());

            return BizPacket.success();
        } catch (Exception e) {
            logger.error("name="+name+",newMobile="+mobile+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Override
    public BizPacket auth4Empl(String name, String mobile, PoiUserData userData){
        PoiUserData existUser = poiUserDao.getPOIUserDataByMobileExcludeId(mobile,userData.getId());
        if(existUser != null){
            return BizPacket.error(HttpStatus.CONFLICT.value(),"抱歉,手机号'"+mobile+"'已被使用!");
        }

        PoiCandidate candidate = null;
        PoiData poiData = null;
        try {
            candidate = poiUserDao.getPoiCandidate(mobile);
            if(candidate == null || StringUtils.isEmpty(candidate.getPoiId())){
                return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(),"抱歉,还没有店铺老板邀请你,暂时无法挂靠店铺!");
            }

            poiData = poiDao.getPoiData(candidate.getPoiId());
            if(poiData == null){
                return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(),"抱歉,邀请你的店铺已经不存在!");
            }

            // 个人资料之缓存的更新
            userData.setPoiId(candidate.getPoiId());
            userData.setName(name);
            userData.setMobile(mobile);
            userData.setIsMaster(EmplIdentity.EMPLOYEE.value());
            userData.setUpdTime(DateTimeUtil.now());
            redisService.addPoiUser(userData);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()+",mobile="+mobile);
        }
        try {
            // 个人资料之持久化
            poiUserDao.update4AuthEmpl(userData.getIsMaster(),userData.getPoiId(),name,mobile,userData.getUpdTime(),userData.getId());


            // 删除邀请
            poiUserDao.removeInvoteById(candidate.getId());

            JSONObject jsonObject = new JSONObject();

            PoiAccountData accountData =  poiAccountDao.getAccountData(poiData.getId());
            PoiBasicData basicData = PoiData.createFrom(poiData,accountData);
            jsonObject.put("poiBasicData",basicData);


            if(!StringUtils.isEmpty(poiData.getEleShopId())){
                jsonObject.put("eleAuth",true);
            }else{
                jsonObject.put("eleAuth",false);
            }
            if(!StringUtils.isEmpty(poiData.getMtAppAuthToken())){
                jsonObject.put("mtAuth",true);
            }else{
                jsonObject.put("mtAuth",false);
            }

            return BizPacket.success(jsonObject);
        } catch (Exception e) {
            logger.error("name="+name+",newMobile="+mobile+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Override
    public BizPacket mobileReplace(PoiUserData currUserData,String newMobile){
        try {

            PoiUserData existUser = poiUserDao.getPOIUserDataByMobileExcludeId(newMobile,currUserData.getId());
            if(existUser != null){
                return BizPacket.error(HttpStatus.CONFLICT.value(),"冲突:新手机号已被绑定在另一个微信上,不允许使用!");
            }


            currUserData.setUpdTime(DateTimeUtil.now());
            currUserData.setMobile(newMobile);
            redisService.addPoiUser(currUserData);
        } catch (IOException e) {
            logger.error("newMobile="+newMobile+",e="+e.getMessage(),e);
        }
        try {
            poiUserDao.updatePOIUserMobile(newMobile,currUserData.getUpdTime(),currUserData.getId());
            return BizPacket.success();
        } catch (Exception e) {
            logger.error("newMobile="+newMobile+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Override
    public BizPacket updatePoiUserName(PoiUserData userData,String name){
        try {
            userData.setName(name);
            userData.setUpdTime(DateTimeUtil.now());
            poiUserDao.updatePOIUserName(name,userData.getUpdTime(),userData.getId());
            redisService.addPoiUser(userData);
            return BizPacket.success();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Override
    public BizPacket login4mobile(String mobile) throws IOException {
        PoiUserData userData = poiUserDao.getPOIUserDataByMobile(mobile);
        if(userData == null){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"用户未找到!");
        }
        BizPacket ret = WechatUtil.check(userData);
        if(ret.getCode() != HttpStatus.OK.value()){
            return ret;
        }

        PoiUserData  _userData = redisService.getPoiUserById(userData.getId());
        if(_userData != null){
            WeichatLoginForm form = buildResponse(_userData);
            return BizPacket.success(form);
        }

        userData.setAccessToken(Generator.uuid());
        userData.setUpdTime(DateTimeUtil.now());
        poiUserDao.updateUserAccessToken(userData.getAccessToken(),userData.getUpdTime(),userData.getId());

        redisService.addPoiUser(userData);

        //long now = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        WeichatLoginForm form = buildResponse(userData);
        return BizPacket.success(form);
    }

    @Override
    public WeichatLoginForm buildResponse(PoiUserData userData){
        WeichatLoginForm form = new WeichatLoginForm();
        form.setAccessToken(userData.getAccessToken());
        form.setNickName(userData.getNickName());
        form.setAvatarUrl(userData.getAvatarUrl());
        form.setName(userData.getName());
        form.setMobile(userData.getMobile());
        form.setIsMaster(userData.getIsMaster());

        if(StringUtils.isEmpty(userData.getPoiId())) {
            return form;
        }

        PoiData poiData = poiDao.getPoiData(userData.getPoiId());
        if(poiData == null) {
            return form;
        }

        PoiAccountData accountData =  poiAccountDao.getAccountData(poiData.getId());
        PoiBasicData basicData = PoiData.createFrom(poiData,accountData);
        form.setPoiBasicData(basicData);
        if(!StringUtils.isEmpty(poiData.getEleShopId())){
            form.setEleAuth(true);
        }
        if(!StringUtils.isEmpty(poiData.getMtAppAuthToken())){
            form.setMtAuth(true);
        }
        return form;
    }

    private List<PoiBasicUserData> transfer(List<PoiUserData> employeeList,String bossId){
        List<PoiBasicUserData> basicUserList = new ArrayList<>(employeeList.size());
        for(PoiUserData u:employeeList){
            if(u.getId().equalsIgnoreCase(bossId)){
                continue;
            }
            PoiBasicUserData data = PoiUserData.createFrom(u);
            basicUserList.add(data);
        }
        return basicUserList;
    }

    @Override
    public BizPacket employeeList(PoiUserData userData){
        List<PoiUserData> employeeList = poiUserDao.getPoiEmployeeList(userData.getPoiId());
        if(employeeList == null || employeeList.isEmpty()){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"暂时没有成员!");
        }
        List<PoiBasicUserData> basicUserList = transfer(employeeList,userData.getId());
        return BizPacket.success(basicUserList);
    }


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

            userData.setUpdTime(DateTimeUtil.now());
            userData.setIsMaster(EmplIdentity.MASTER.value());
            poiUserDao.updatePoiUserMaster(userData.getIsMaster(),userData.getUpdTime(),userData.getId());
            //redisService.addPoiUser(userData);
            redisService.onUserRemoved(userData.getId());

            boss.setUpdTime(DateTimeUtil.now());
            boss.setIsMaster(EmplIdentity.EMPLOYEE.value());
            poiUserDao.updatePoiUserMaster(boss.getIsMaster(),boss.getUpdTime(),boss.getId());
            redisService.addPoiUser(boss);

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
            if(!userData.getPoiId().equalsIgnoreCase(boss.getPoiId())){
                return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你删除的不是本店成员!");
            }

            userData.setPoiId("");
            userData.setUpdTime(DateTimeUtil.now());
            poiUserDao.removeUserFomPOI("",userData.getUpdTime(),userData.getId());
            redisService.onUserRemoved(userData.getId());

            return BizPacket.success();
        } catch (Exception e) {
            logger.error("boss="+boss+",userId="+boss+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }
}