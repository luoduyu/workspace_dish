package com.amt.wechat.controller.poi;

import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.model.poi.MaterialData;
import com.amt.wechat.service.poi.PoiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  店铺类controller
 *
 * @author adu Create on 2019-01-03 20:24
 * @version 1.0
 */
@RestController
public class PoiController {

    private @Resource PoiService poiService;


    @GetMapping(value = "/poi/material/list")
    public BizPacket poiMaterialList(){
        List<MaterialData> list =  poiService.getPoiMaterialDataList();
        return BizPacket.success(list);
    }
}