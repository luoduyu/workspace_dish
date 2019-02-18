package com.amt.wechat.controller.wechat.decoration;

import com.amt.wechat.dao.decoration.MaterialDao;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.model.decoration.MaterialData;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping(value = "/decoration/material/list")
    public BizPacket poiMaterialList(){
        List<MaterialData> list =  materialDao.getPoiMaterialDataList();
        return BizPacket.success(list);
    }
}
