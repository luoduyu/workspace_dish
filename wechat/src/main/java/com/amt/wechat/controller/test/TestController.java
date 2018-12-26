package com.amt.wechat.controller.test;

import com.amt.wechat.domain.packet.BizPacket;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-18 16:46
 * @version 1.0
 */
@RestController
public class TestController {


    @RequestMapping("/wx/test")
    public BizPacket test(String code){
        System.out.println("code="+code);
        return BizPacket.success();
    }
}
