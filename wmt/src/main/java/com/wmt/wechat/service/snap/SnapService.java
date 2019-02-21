package com.wmt.wechat.service.snap;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.wechat.form.order.OrderSubmitForm;
import com.wmt.wechat.model.poi.PoiUserData;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *   抢购业务
 *
 * @author adu Create on 2019-01-28 15:07
 * @version 1.0
 */
public interface SnapService {


    public BizPacket snapCateList();
    public BizPacket todaySnapGoodsList(int cateId);


    public BizPacket snapOrderSubmit(PoiUserData userData, OrderSubmitForm orderSubmitForm);
}
