package com.amt.wechat.controller.dish;

import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.model.dish.DishCateData;
import com.amt.wechat.service.dish.DishService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-29 16:29
 * @version 1.0
 */
@RestController
public class DishController {
    private @Resource  DishService dishService;

    /**
     * 海报全部分类
     * @return
     */
    @RequestMapping(value="/dish/allcate")
    public BizPacket findPosterCate(){
        List<DishCateData> list = dishService.getPosterCateList();
        return BizPacket.success(list);
    }
}
