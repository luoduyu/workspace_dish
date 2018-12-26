package com.amt.wechat.service.poster.impl;

import com.amt.wechat.common.PosterOrderClause;
import com.amt.wechat.dao.poster.PosterDAO;
import com.amt.wechat.model.poster.*;
import com.amt.wechat.service.poster.IPosterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-25 14:48
 * @version 1.0
 */
@Service("posterService")
public class PosterServiceImpl implements IPosterService {

    private @Resource PosterDAO posterDAO;

    @Override
    public List<SequencePoster> getRecommendPosterList() {

        // TODO 后期改为从redis中取
        Map<String, RecommendPoster> map =  posterDAO.getRecommendPoster();

        if(map == null || map.isEmpty()){
            return Collections.emptyList();
        }

        StringBuilder ids = new StringBuilder();
        for(String posterId :map.keySet()){
            ids.append(posterId).append(",");
        }
        ids.deleteCharAt(ids.length()-1);
        List<SequencePoster> recommendList =  posterDAO.getPosterListByIds(ids.toString());
        for(SequencePoster p:recommendList){
            RecommendPoster rp = map.get(p.getId());
            if(rp == null){
                continue;
            }
            p.setShowSeq(rp.getShowSeq());
        }
        return recommendList;
    }

    @Override
    public void addPoster(Poster poster){
        posterDAO.addPoster(poster);
    }

    @Override
    public List<PosterCate> getPosterCateList() {
        return posterDAO.getPosterCate();
    }


    @Override
    public List<SequencePoster> getIntelligent(int index,int pageSize) {
        List<SequencePoster> list =  posterDAO.getPosterList(index * pageSize,pageSize);

        // 取销量最高的8条
        Map<String,SequencePoster>  topPosterMap = getTopPoster(8);
        List<SequencePoster> retList = new ArrayList<>(topPosterMap.values());

        // 取最新上线的，且去重
        for(SequencePoster o:list){
            if(topPosterMap.containsKey(o.getId())){
                continue;
            }
            retList.add(o);
        }
        return retList;
    }

    /**
     * 获得销量最高的8条
     *
     * @param pageSize
     * @return
     */
    private Map<String,SequencePoster> getTopPoster(int pageSize) {

        // TODO 后期改为基于redis实现
        Map<String,TopPoster> map =  posterDAO.getTopPoster(1,30,pageSize);
        if(map == null || map.isEmpty()){
            return Collections.emptyMap();
        }

        StringBuilder ids = new StringBuilder();
        for(String posterId:map.keySet()){
            ids.append(posterId).append(",");
        }
        ids.deleteCharAt(ids.length() -1);

        Map<String,SequencePoster> retMap = new HashMap<>();

        List<SequencePoster> list = posterDAO.getPosterListByIds(ids.toString());
        for(SequencePoster o:list){
            TopPoster tp = map.get(o.getId());
            if(tp == null){
                continue;
            }
            o.setShowSeq(tp.getSales());
            retMap.put(o.getId(),o);
        }
        return retMap;
    }

    @Override
    public List<SequencePoster> getPosterListByCate(int cateId, Integer index, Integer pageSize, PosterOrderClause orderClause) {
        if(orderClause == PosterOrderClause.SALES){
            List<SequencePoster> list = posterDAO.getPosterListBySales(cateId,index,pageSize);
            return list;
        }

        List<SequencePoster> list = posterDAO.getPosterListByClause(cateId,orderClause.getClause(),index,pageSize);
        return list;
    }

    @Override
    public Poster getPosterDetail(String posterId) {
        return posterDAO.getPosterDetail(posterId);
    }
}