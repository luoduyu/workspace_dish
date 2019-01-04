package com.amt.wechat.service.dish;

import com.amt.wechat.model.dish.DishCateData;

import java.util.List;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-29 16:29
 * @version 1.0
 */
public interface DishService {

    /**
     * 所有的海报分类数据
     * @return
     */
    public List<DishCateData> getPosterCateList();
}
