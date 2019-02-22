package com.wmt.mgr.service.user.impl;

import com.wmt.commons.domain.id.Generator;
import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.commons.util.DateTimeUtil;
import com.wmt.mgr.dao.user.MgrUserDao;
import com.wmt.mgr.form.MgrUserForm;
import com.wmt.mgr.model.user.MgrUserData;
import com.wmt.mgr.service.redis.RedisService;
import com.wmt.mgr.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

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

}
