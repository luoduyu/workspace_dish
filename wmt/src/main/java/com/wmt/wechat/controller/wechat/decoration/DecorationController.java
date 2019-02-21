package com.wmt.wechat.controller.wechat.decoration;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.wechat.dao.decoration.MaterialDao;
import com.wmt.wechat.model.decoration.MaterialData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * 装修类controller
 *
 * @author adu Create on 2019-01-11 11:17
 * @version 1.0
 */
@RestController
public class DecorationController {
    private @Resource MaterialDao materialDao;

    @RequestMapping(value = "/decoration/material/list")
    public BizPacket poiMaterialList(){
        List<MaterialData> list =  materialDao.getPoiMaterialDataList();
        return BizPacket.success(list);
    }
}
