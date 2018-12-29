package com.amt.wechat.controller.go;

import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.model.go.OperationalApp;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-29 16:41
 * @version 1.0
 */
@RestController
public class GoController {

    @RequestMapping(value = "/go/",method = RequestMethod.POST,produces = {"application/json","text/html"})
    public BizPacket appSubmit(OperationalApp app){



        return BizPacket.success();
    }
}
