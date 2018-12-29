package com.amt.wechat.dao.dish;

import com.amt.wechat.model.dish.DishCate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-29 16:31
 * @version 1.0
 */
@Repository("dishDao")
@Mapper
public interface DishDao {

    @Select("SELECT * FROM dish_cate ORDER BY nameSeq ASC,subSeq ASC")
    public List<DishCate> getPosterCate();
}
