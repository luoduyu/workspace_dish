package com.amt.wechat.service.poster;

import com.amt.wechat.common.PosterOrderClause;
import com.amt.wechat.model.poster.Poster;
import com.amt.wechat.model.poster.SequencePoster;

import java.util.List;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-25 14:47
 * @version 1.0
 */
public interface IPosterService {

    /**
     * 猜你想要的海报
     * @return
     */
    public List<SequencePoster> getRecommendPosterList();

    public void addPoster(Poster poster);

    /**
     *
     * 智能推荐
     *
     * @param index
     * @param pageSize
     * @return
     */
    public List<SequencePoster> getIntelligent(int index,int pageSize);


    /**
     * 分类别的海报列表
     * @param index
     * @param pageSize
     * @param cateId 类别Id
     * @param orderClause 0：全部;1:最新作品;2:价格最低;3:人气作品;
     * @return
     */
    public List<SequencePoster> getPosterListByCate(int cateId, Integer index, Integer pageSize, PosterOrderClause orderClause);


    public Poster getPosterDetail(String posterId);
}