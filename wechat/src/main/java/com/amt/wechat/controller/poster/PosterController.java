package com.amt.wechat.controller.poster;

import com.amt.wechat.common.PosterOrderClause;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.form.SeqPosterWrapper;
import com.amt.wechat.model.poster.Poster;
import com.amt.wechat.model.poster.PosterCate;
import com.amt.wechat.model.poster.SequencePoster;
import com.amt.wechat.service.poster.IPosterService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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


    @RequestMapping(value = "/poster/recommanded/list",method = {RequestMethod.POST,RequestMethod.GET})
    public BizPacket findPosterList(){
        List<SequencePoster> list =  posterService.getRecommendPosterList();
        return BizPacket.success(list);
    }

    /**
     * 海报全部分类
     * @return
     */
    @RequestMapping(value="/poster/allcate")
    public BizPacket findPosterCate(){
        List<PosterCate> list = posterService.getPosterCateList();
        return BizPacket.success(list);
    }

    @RequestMapping(value = "/poster/intellij/list",method = {RequestMethod.POST,RequestMethod.GET})
    public BizPacket findIntelligent(Integer index,Integer pageSize){
        if(index == null || index <0 || index >Integer.MAX_VALUE){
            index = 0;
        }
        if(pageSize == 0 || pageSize < 0 || pageSize >= 600){
            pageSize = 15;
        }

        List<SequencePoster> result=  posterService.getIntelligent(index,pageSize);
        SeqPosterWrapper wapper = new SeqPosterWrapper(result.size(),result);
        return BizPacket.success(wapper);
    }


    /**
     * 分类别的海报列表
     * @param cateId 类别Id
     * @param orderClause 0：全部;1:最新作品;2:价格最低;3:人气作品;
     * @return
     */
    @RequestMapping(value="/poster/cate/list")
    public BizPacket findPosterListByCate(@RequestParam("cateId") int cateId, Integer index, Integer pageSize, Integer orderClause){
        if(cateId <= 0 || cateId >= Integer.MAX_VALUE){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"类别参数错误!");
        }
        if(orderClause == 0){
            orderClause = 0;
        }
        PosterOrderClause clause = PosterOrderClause.valueOf(orderClause);
        if(clause == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"排序从句参数错误!");
        }
        if(index == null || index <0 || index > Integer.MAX_VALUE){
            index = 0;
        }

        if(pageSize == null || pageSize <0 || pageSize > 600){
            pageSize = 15;
        }

        List<SequencePoster> list =  posterService.getPosterListByCate(cateId,index,pageSize,clause);
        return BizPacket.success(list);
    }

    @RequestMapping(value="/poster/cate/detail")
    public BizPacket findPosterDetail(@RequestParam("posterId") String posterId){
        Poster poster =  posterService.getPosterDetail(posterId);
        if(poster == null){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"未找到!");
        }
        return BizPacket.success(poster);
    }

    @RequestMapping(value="/poster/addfavorite")
    public BizPacket addFavorite(@RequestParam("posterId") String posterId){
        // TODO
        
        return BizPacket.success();
    }
}