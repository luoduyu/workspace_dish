package com.wmt.mgr.service.mgr.user;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.form.mgr.user.UserForm;
import com.wmt.mgr.model.mgr.user.MgrUserData;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-22 13:22
 * @version 1.0
 */
public interface UserService {
    /**
     * 根据授权个人微信信息和存储至HttpSession中的手机号注册(若未注册)并登录
     * @param userData
     * @return
     */
    public BizPacket loginByMobile(MgrUserData userData);

    public void onLoginByAccessToken(MgrUserData userData);

    public BizPacket userList(String name, String mobile, Integer isEnabled, Integer index, Integer pageSize);

    public BizPacket userAdd(MgrUserData admin, UserForm addForm);

    public BizPacket userEdit(MgrUserData admin, UserForm form);
}
