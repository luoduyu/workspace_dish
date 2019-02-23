package com.wmt.mgr.service.mgr.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.wmt.commons.domain.id.Generator;
import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.commons.util.DateTimeUtil;
import com.wmt.mgr.dao.mgr.user.MgrUserDao;
import com.wmt.mgr.form.mgr.user.MgrUserForm;
import com.wmt.mgr.form.mgr.user.UserForm;
import com.wmt.mgr.model.mgr.user.MgrUserData;
import com.wmt.mgr.service.mgr.user.UserService;
import com.wmt.mgr.service.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private @Resource RedisService redisService;
    private @Resource   MgrUserDao mgrUserDao;

    @Override
    public BizPacket loginByMobile(MgrUserData mgrUserData) {
        logger.info("用户[userData={}]正在登录!",mgrUserData);
        try {

            String oldAccessToken = mgrUserData.getAccessToken();
            mgrUserData.setAccessToken(Generator.uuid());
            mgrUserData.setUpdTime(DateTimeUtil.now());

            mgrUserDao.updateMgrUser4Login(mgrUserData);
            redisService.addMgrUser(mgrUserData,oldAccessToken);

            //long now = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            MgrUserForm form = MgrUserForm.buildResponse(mgrUserData);
            return BizPacket.success(form);
        } catch (IOException e) {
            logger.error("e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Override
    public void onLoginByAccessToken(MgrUserData mgrUserData){
        mgrUserData.setUpdTime(DateTimeUtil.now());
        mgrUserDao.updateMgrUser4Login(mgrUserData);
    }

    @Override
    public BizPacket userList(String name, String mobile, Integer isEnabled, Integer index, Integer pageSize) {
        if(name != null){
            name = name.trim();
        }
        if(mobile != null){
            mobile = mobile.trim();
        }

        JSONObject jsonObject = new JSONObject();

        Integer total = mgrUserDao.countUserData(name,mobile,isEnabled);
        jsonObject.put("total",total);

        List<MgrUserData> list  = mgrUserDao.getMgrUserDataList(name,mobile,isEnabled,index * pageSize,pageSize);
        jsonObject.put("list",list);
        return BizPacket.success(jsonObject);
    }

    @Override
    public BizPacket userAdd(MgrUserData admin, UserForm form) {
        MgrUserData user = createUser(form);
        mgrUserDao.addMgrUser(user);
        return BizPacket.success(user.getId());
    }

    private MgrUserData createUser(UserForm form){
        MgrUserData userData = new MgrUserData();
        userData.setAccessToken("");

        userData.setCreateTime(DateTimeUtil.now());
        userData.setUpdTime(DateTimeUtil.now());
        userData.setGender(1);
        userData.setIsAccountNonExpired(1);
        userData.setIsAccountNonLocked(1);
        userData.setIsCredentialsNonExpired(1);
        userData.setMobile(form.getMobile().trim());
        userData.setName(form.getName().trim());
        userData.setNickName(form.getName().trim());
        userData.setIsEnabled(form.getIsEnabled());
        userData.setPassword(form.getPassword());

        return userData;
    }

    @Override
    public BizPacket userEdit(MgrUserData admin, UserForm form) {
        MgrUserData mgrUserData = mgrUserDao.getMgrUserDataById(form.getId());
        if(mgrUserData == null){
            return BizPacket.error(HttpStatus.FORBIDDEN.value(),"不存在的帐户!id="+form.getId());
        }

        mgrUserData.setName(form.getName());
        mgrUserData.setMobile(form.getMobile());
        mgrUserData.setPassword(form.getPassword());
        mgrUserData.setIsEnabled(form.getIsEnabled());
        mgrUserDao.updateMgrUser(mgrUserData);
        return BizPacket.success();
    }
}
