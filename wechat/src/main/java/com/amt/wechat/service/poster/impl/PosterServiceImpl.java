package com.amt.wechat.service.poster.impl;

import com.amt.wechat.dao.poster.PosterDAO;
import com.amt.wechat.model.poster.Poster;
import com.amt.wechat.model.poster.PosterCate;
import com.amt.wechat.service.poster.IPosterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    public List<Poster> getPosterList(int index, int pageSize) {
        return posterDAO.getPosterList(index*pageSize,pageSize);
    }

    @Override
    public void addPoster(Poster poster){
        posterDAO.addPoster(poster);
    }

    @Override
    public List<PosterCate> getPosterCateList() {
        return posterDAO.getPosterCate();
    }
}
