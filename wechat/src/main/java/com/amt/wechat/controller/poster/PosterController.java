package com.amt.wechat.controller.poster;

import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.model.poster.Poster;
import com.amt.wechat.model.poster.PosterCate;
import com.amt.wechat.service.poster.IPosterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (c) 2018 by CANSHU
 *
 *
 * @author adu Create on 2018-12-25 16:20
 * @version 1.0
 */
@RestController
public class PosterController {

    private @Resource IPosterService posterService;


    @RequestMapping(value = "/poster/want/list",method = {RequestMethod.POST,RequestMethod.GET})
    public BizPacket findPosterList(Integer index, Integer pageSize){
        if(index == null){
            index = 0;
        }
        if(pageSize == null){
            pageSize = 10;
        }

        List<Poster> list =  posterService.getPosterList(index,pageSize);
        return BizPacket.success(list);
    }

    @RequestMapping(value="/poster/cate/list")
    public BizPacket findPosterCate(){
        List<PosterCate> list = posterService.getPosterCateList();
        return BizPacket.success(list);
    }
}