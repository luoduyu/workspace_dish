package com.wmt.mgr.service.wechat.poi.impl;

import com.alibaba.fastjson.JSONObject;
import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.dao.wechat.poi.PoiAccountDao;
import com.wmt.mgr.model.poi.PoiAccountForm;
import com.wmt.mgr.service.wechat.poi.PoiAccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author lujunp Create on 2019/2/28 13:53
 * @version 1.0
 */
@Service("poiAccountServiceImpl")
public class PoiAccountServiceImpl implements PoiAccountService {

    @Resource
    private PoiAccountDao poiAccountDao;

    @Override
    public BizPacket queryPoiAccountList(String branchName, String poiUserName,
                                         String poiUserMobile, int index, int pageSize) {

        Integer total = poiAccountDao.countPoiAccount(branchName, poiUserName, poiUserMobile);
        if (total == null) {
            total = 0;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", total);
        if (total <= 0) {
            jsonObject.put("list", Collections.emptyList());
            return BizPacket.success(jsonObject);
        }

        List<PoiAccountForm> list = poiAccountDao.findPoiAccountList(branchName, poiUserName, poiUserMobile, index * pageSize, pageSize);
        jsonObject.put("list", list);
        return BizPacket.success(jsonObject);

    }
}
