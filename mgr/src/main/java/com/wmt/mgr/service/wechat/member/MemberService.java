package com.wmt.mgr.service.wechat.member;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.form.wechat.member.CardForm;
import com.wmt.mgr.model.mgr.user.MgrUserData;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-23 17:31
 * @version 1.0
 */
public interface MemberService {

    public BizPacket cardAdd(MgrUserData admin, CardForm form);
    public BizPacket cardEdit(MgrUserData admin, CardForm form);
    public BizPacket cardRM(MgrUserData admin, int id);
    public BizPacket cardDetail(MgrUserData admin, int id);

    public BizPacket cardShowSeqUpdate(MgrUserData admin, int cardId,int showSeq);

}
