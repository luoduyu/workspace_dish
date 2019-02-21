package com.wmt.wechat.controller.wechat.snap;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.wechat.service.snap.SnapService;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  抢购相关
 *
 * @author adu Create on 2019-01-28 11:57
 * @version 1.0
 */
@RestController
public class SnapController {
    private @Resource SnapService snapService;

    @GetMapping("/snap/cate/list")
    public BizPacket snapCateList(){
        return snapService.snapCateList();
    }


    /**
     * 抢购物品列表
     * @return
     */
    @PostMapping("/snap/cate/goodslist")
    public BizPacket snapGoodsList(Integer snapCateId){
        if(StringUtils.isEmpty(snapCateId)){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"必传参数(snapCateId):"+snapCateId);
        }

        return snapService.todaySnapGoodsList(snapCateId);
    }
}
