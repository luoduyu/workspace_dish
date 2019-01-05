package com.amt.wechat.controller.poster;

import com.amt.wechat.common.PosterOrderClause;
import com.amt.wechat.controller.base.BaseController;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.form.SeqPosterWrapper;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.model.poster.PosterData;
import com.amt.wechat.model.poster.SequencePoster;
import com.amt.wechat.service.poster.IPosterService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
public class PosterController extends BaseController {

    private @Resource IPosterService posterService;


    /**
     * 猜你想要海报列表接口
     * @return
     */
    @RequestMapping(value = "/poster/recommanded/list",method = {RequestMethod.POST,RequestMethod.GET})
    public BizPacket findPosterList(){
        List<SequencePoster> list =  posterService.getRecommendPosterList();
        return BizPacket.success(list);
    }


    /**
     * 智能推荐
     * @param index
     * @param pageSize
     * @return
     */
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
     * @param orderClause 0：全部;1:最新作品;2:价格最低;3:人气作品(按销量倒排)
     * @return
     */
    @RequestMapping(value="/poster/cate/list")
    public BizPacket findPosterListByCate(@RequestParam("cateId") Integer cateId, Integer index, Integer pageSize, Integer orderClause){
        if(cateId == null || cateId <= 0 || cateId >= Integer.MAX_VALUE){
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
    public BizPacket findPosterDetail(@RequestParam("posterId") Integer posterId){
        PosterData posterData =  posterService.getPosterDetail(posterId);
        if(posterData == null){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"未找到!");
        }
        return BizPacket.success(posterData);
    }

    @RequestMapping(value="/poster/addfavorite")
    public BizPacket addFavorite(@RequestParam("posterId") Integer posterId){
        // TODO
        
        return BizPacket.success();
    }

    @GetMapping("/poster/loginTest")
    public BizPacket testEffect(){
        PoiUserData user = getUser();
        return BizPacket.success(user);
    }
}