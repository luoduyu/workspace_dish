/*
watermark参数说明：

        参数	类型	说明
        watermark	OBJECT	数据水印
        appid	String	敏感数据归属appid，开发者可校验此参数与自身appid是否一致
        timestamp	DateInt	敏感数据获取的时间戳, 开发者可以用于数据时效性校验
*/

package com.wmt.wechat.service.poi.impl;

import com.alibaba.fastjson.JSONObject;
import com.wmt.commons.constants.RedisConstants;
import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.commons.util.DateTimeUtil;
import com.wmt.dlock.lock.DistributedLock;
import com.wmt.wechat.dao.globalsetting.GlobalSettingDao;
import com.wmt.wechat.dao.poi.PoiAccountDao;
import com.wmt.wechat.dao.poi.PoiDao;
import com.wmt.wechat.dao.poi.PoiUserDao;
import com.wmt.wechat.dao.poi.PoiUserWXCodeDao;
import com.wmt.wechat.domain.id.Generator;
import com.wmt.wechat.domain.util.WechatUtil;
import com.wmt.wechat.form.basic.WeichatLoginForm;
import com.wmt.wechat.form.poi.PoiForm;
import com.wmt.wechat.model.poi.*;
import com.wmt.wechat.service.poi.EmplIdentity;
import com.wmt.wechat.service.poi.PoiUserService;
import com.wmt.wechat.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    private @Resource PoiUserWXCodeDao poiUserWXCodeDao;
    private @Resource GlobalSettingDao globalSettingDao;
    private @Resource StringRedisTemplate stringRedisTemplate;
    private static Long ACQUIRE_TIMEOUT_IN_MILLIS = (long) Integer.MAX_VALUE;


    @Override
    public BizPacket weichatLogin(String code, String encryptedData, String iv, String inviterId) {
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

                userData = createUser(sessionKeyAndOpenid,userJson,inviterId);
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



    private PoiUserData createUser(JSONObject sessionKeyAndOpenid,JSONObject userJson,String inviterId){
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
        data.setShareBalance(0);
        data.setName("");
        data.setCountryCode("中国");

        data.setPassword("");
        if(inviterId != null){
            data.setInviterId(inviterId.trim());
        }else{
            data.setInviterId("");
        }

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
    public BizPacket employeeSet(String name, String mobile, PoiUserData userData){
        PoiUserData existUser = poiUserDao.getPOIUserDataByMobileExcludeId(mobile,userData.getId());
        if(existUser != null){
            return BizPacket.error(HttpStatus.CONFLICT.value(),"抱歉,手机号'"+mobile+"'已被使用!");
        }

        try {
            // 个人资料更新
            userData.setName(name);
            userData.setMobile(mobile);
            userData.setUpdTime(DateTimeUtil.now());
            redisService.addPoiUser(userData);

        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()+",mobile="+mobile);
        }
        try {
            // 个人资料之持久化
            poiUserDao.employeeSet(name,mobile,userData.getUpdTime(),userData.getId());
            return BizPacket.success();
        } catch (Exception e) {
            logger.error("name="+name+",newMobile="+mobile+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Override
    public BizPacket poiBind(PoiUserData userData, PoiCandidate candidate){
        PoiData poiData = null;
        try {

            poiData = poiDao.getPoiData(candidate.getPoiId());
            if(poiData == null){
                return BizPacket.error(HttpStatus.PRECONDITION_FAILED.value(),"抱歉,邀请你的店铺已经不存在!");
            }

            // 个人资料之缓存的更新
            userData.setPoiId(poiData.getId());
            userData.setIsMaster(EmplIdentity.EMPLOYEE.value());
            userData.setUpdTime(DateTimeUtil.now());
            redisService.addPoiUser(userData);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage()+",candidate="+candidate);
        }
        try {
            // 个人资料之持久化
            poiUserDao.update4PoiBind(userData.getIsMaster(),userData.getPoiId(),userData.getUpdTime(),userData.getId());

            // 删除邀请
            poiUserDao.removeInvoteById(candidate.getId());

            JSONObject jsonObject = new JSONObject();
            String masterMobile = poiUserDao.getMasterMobile(poiData.getId(),EmplIdentity.MASTER.value());
            PoiAccountData accountData =  poiAccountDao.getAccountData(poiData.getId());
            PoiForm basicData = PoiData.createFrom(poiData,accountData,masterMobile);
            jsonObject.put("poiBasicData",basicData);

            return BizPacket.success(jsonObject);
        } catch (Exception e) {
            logger.error("userData="+userData+",candidate="+candidate+",e="+e.getMessage(),e);
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
        form.setPuid(userData.getId());
        form.setAccessToken(userData.getAccessToken());
        form.setNickName(userData.getNickName());
        form.setAvatarUrl(userData.getAvatarUrl());
        form.setName(userData.getName());
        form.setMobile(userData.getMobile());
        form.setIsMaster(userData.getIsMaster());
        form.setShareBalance(userData.getShareBalance());

        if(StringUtils.isEmpty(userData.getPoiId())) {
            return form;
        }

        PoiData poiData = poiDao.getPoiData(userData.getPoiId());
        if(poiData == null) {
            return form;
        }

        String masterMobile = poiUserDao.getMasterMobile(poiData.getId(),EmplIdentity.MASTER.value());
        PoiAccountData accountData =  poiAccountDao.getAccountData(poiData.getId());
        PoiForm basicData = PoiData.createFrom(poiData,accountData,masterMobile);
        form.setPoiBasicData(basicData);

        return form;
    }

    private List<PoiBasicUserData> transfer(List<PoiUserData> employeeList, String bossId){
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

            PoiCandidate candidate = poiUserDao.getPoiCandidate(mobile,bossData.getPoiId());
            if(candidate != null) {
                return BizPacket.success();
            }

            PoiData poiData =  poiDao.getPoiData(bossData.getPoiId());
            candidate = new PoiCandidate();
            candidate.setUserId(bossData.getId());
            candidate.setCreateTime(DateTimeUtil.now());
            candidate.setMobile(mobile);
            candidate.setPoiId(bossData.getPoiId());
            candidate.setPoiName(poiData.getName());

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
            userData.setIsMaster(EmplIdentity.NONE.value());

            poiUserDao.removeUserFomPOI(userData.getIsMaster(),"",userData.getUpdTime(),userData.getId());
            redisService.onUserRemoved(userData.getId());

            return BizPacket.success();
        } catch (Exception e) {
            logger.error("boss="+boss+",userId="+boss+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Override
    public void onInviteSucc(String inviterId,String inviteeId,PoiMemberRDData rd) {
        try {
            Integer amount = poiUserWXCodeDao.countIncomeByPoiId(rd.getPoiId());
            if(amount != null && amount >=1){
                logger.info("基于店铺poiId={}的邀请奖励已经发放过了!邀请者id={},被邀请者id={}",rd.getPoiId(),inviterId,rd.getUserId());
                return;
            }
            Integer  share =  globalSettingDao.getShareReward();
            if(share == null){
                logger.info("未设置邀请奖励!poiId={},邀请者id={},被邀请者id={}",rd.getPoiId(),inviterId,rd.getUserId());
                return;
            }

            // 个人帐户锁
            DistributedLock userDLock = new DistributedLock(stringRedisTemplate, RedisConstants.CANSHU_POI_UID+inviterId);
            try {
                userDLock.acquire(ACQUIRE_TIMEOUT_IN_MILLIS);

                PoiUserData inviterData = poiUserDao.getPOIUserDataById(inviterId);

                UserIncomeData data = createIncomeRD(inviterData, inviteeId, rd, share);
                poiUserWXCodeDao.addInvitationIncome(data);


                poiUserDao.updateShareBlance(inviterId, share);
                PoiUserData userData = redisService.getPoiUserById(inviterId);
                if (userData != null) {
                    userData.setShareBalance(userData.getShareBalance() + share);
                    redisService.addPoiUser(userData);
                }

                logger.info("邀请奖励发放成功!inviterId={},poiId={},inviteeId={},奖励额(分)={}", inviterId, rd.getPoiId(), rd.getUserId(), share);
            }finally {
                userDLock.release();
            }
        } catch (Exception e) {
            logger.error("发放邀请奖励时出错!inviterId="+inviterId+",poiId="+rd.getPoiId()+",inviteeId="+rd.getUserId()+",e="+e.getMessage(),e);
        }
    }

    private UserIncomeData createIncomeRD(PoiUserData inviterData, String inviteeId,PoiMemberRDData rd,int share){
        UserIncomeData data = new UserIncomeData();
        data.setUserId(inviterData.getId());
        data.setUserAvatarUrl(inviterData.getAvatarUrl());
        data.setUserNickName(inviterData.getNickName());
        data.setShare(share);

        data.setCreateDate(DateTimeUtil.now());
        data.setPoiId(rd.getPoiId());
        data.setInviteeId(inviteeId);
        data.setDurationUnit(rd.getDurationUnit());
        data.setDuration(rd.getDuration());

        PoiUserData inviteeData = poiUserDao.getPOIUserDataById(inviteeId);
        if(inviteeData != null){
            data.setNickName(inviteeData.getNickName());
            data.setAvatarUrl(inviteeData.getAvatarUrl());
        }else{
            data.setNickName("");
            data.setAvatarUrl("");
        }
        return data;
    }
}