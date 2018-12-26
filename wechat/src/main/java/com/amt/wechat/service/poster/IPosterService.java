package com.amt.wechat.service.poster;

import com.amt.wechat.model.poster.Poster;
import com.amt.wechat.model.poster.PosterCate;

import java.util.List;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-25 14:47
 * @version 1.0
 */
public interface IPosterService {

    public List<Poster> getPosterList(int index, int pageSize);

    public void addPoster(Poster poster);

    public List<PosterCate> getPosterCateList();
}
