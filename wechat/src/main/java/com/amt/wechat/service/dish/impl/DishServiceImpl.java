package com.amt.wechat.service.dish.impl;

import com.amt.wechat.dao.dish.DishDao;
import com.amt.wechat.model.dish.DishCate;
import com.amt.wechat.service.dish.DishService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-29 16:30
 * @version 1.0
 */
@Service("dishService")
public class DishServiceImpl implements DishService {

    private @Resource  DishDao dishDao;

    @Override
    public List<DishCate> getPosterCateList() {
        return dishDao.getPosterCate();
    }
}