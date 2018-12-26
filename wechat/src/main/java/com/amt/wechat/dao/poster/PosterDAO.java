package com.amt.wechat.dao.poster;

import com.amt.wechat.domain.handler.MyJSONArrayHandler;
import com.amt.wechat.domain.handler.MyJSONHandler;
import com.amt.wechat.model.poster.Poster;
import com.amt.wechat.model.poster.PosterCate;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2018 by CANSHU
 *
 *  海报类DAO
 *
 * @author adu Create on 2018-12-25 14:27
 * @version 1.0
 */

@Repository("posterDAO")
@Mapper
public interface PosterDAO {

    @Select("SELECT * FROM poster LIMIT #{index},#{pageSize}")
    @Results({
            @Result(property = "banner",column = "banner",typeHandler = MyJSONArrayHandler.class),
            @Result(property = "rendering",column = "rendering",typeHandler = MyJSONArrayHandler.class)
    })
    public List<Poster> getPosterList(int index,int pageSize);

    @Insert("INSERT INTO poster (id, platform,cateId, title,banner,rendering, mPrice,price, cTime, uTime)" +
            "VALUES(#{id},#{platform},#{cateId},#{title},#{banner,jdbcType=OTHER,typeHandler=com.amt.wechat.domain.handler.MyJSONArrayHandler},#{rendering,jdbcType=OTHER,typeHandler=com.amt.wechat.domain.handler.MyJSONArrayHandler},#{mPrice},#{price},#{cTime},#{uTime})")
    public void addPoster(Poster poster);

    @Select("SELECT * FROM poster_cate ORDER BY nameSeq ASC,subSeq ASC")
    public List<PosterCate> getPosterCate();
}