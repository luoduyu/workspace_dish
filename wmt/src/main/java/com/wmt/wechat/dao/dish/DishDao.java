package com.wmt.wechat.dao.dish;

import com.wmt.wechat.model.dish.DishCateData;
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

    @Select("SELECT * FROM dish_cate ORDER BY nameSeq ASC,subSeq ASC LIMIT 500")
    public List<DishCateData> getPosterCate();


    @Select("SELECT COUNT(cateId) FROM dish_cate WHERE cateId=#{cateId}")
    public int countByCateId(int cateId);
}
