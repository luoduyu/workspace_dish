package com.amt.wechat.dao.poster;

import com.amt.wechat.domain.handler.MyJSONArrayHandler;
import com.amt.wechat.model.poster.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

    @Select("SELECT * FROM poster WHERE isEnabled=1 ORDER BY uTime DESC LIMIT #{index},#{pageSize}")
    @Results({
            @Result(property = "banner",column = "banner",typeHandler = MyJSONArrayHandler.class),
            @Result(property = "rendering",column = "rendering",typeHandler = MyJSONArrayHandler.class)
    })
    public List<SequencePoster> getPosterList(int index,int pageSize);


    @Select("SELECT * FROM poster WHERE isEnabled=1 AND id IN (${ids})")
    @Results({
            @Result(property = "banner",column = "banner",typeHandler = MyJSONArrayHandler.class),
            @Result(property = "rendering",column = "rendering",typeHandler = MyJSONArrayHandler.class)
    })
    public List<SequencePoster> getPosterListByIds(@Param("ids")String ids);


    @Insert("INSERT INTO poster (id, platform,cateId, title,banner,rendering, mPrice,price, cTime, uTime)" +
            "VALUES(#{id},#{platform},#{cateId},#{title},#{banner,jdbcType=OTHER,typeHandler=com.amt.wechat.domain.handler.MyJSONArrayHandler},#{rendering,jdbcType=OTHER,typeHandler=com.amt.wechat.domain.handler.MyJSONArrayHandler},#{mPrice},#{price},#{cTime},#{uTime})")
    public void addPoster(Poster poster);





    @Select("SELECT posterId,showSeq FROM poster_recommend ORDER BY showSeq ASC")
    @MapKey("posterId")
    public Map<String, RecommendPoster> getRecommendPoster();


    /**
     * 最近一个月/周/天销量最高的8-10条
     *
     * @param timeUnit
     * @param expiresIn
     * @param pageSize
     * @return
     */
    @Select("SELECT posterId,sales FROM poster_sales_top WHERE timeUnit=#{timeUnit} AND expiresIn=#{expiresIn} ORDER BY sales DESC LIMIT #{pageSize}")
    @MapKey("posterId")
    public Map<String, TopPoster> getTopPoster(int timeUnit, int expiresIn, int  pageSize);


    /**
     * 分类别的、按销量倒序排列的海报列表
     * @return
     */
    @Select("SELECT p.*,pt.sales AS showSeq FROM poster p LEFT JOIN poster_sales_top pt ON p.id=pt.posterId WHERE p.isEnabled=1 AND p.cateId = #{cateId} AND pt.timeUnit=1 AND pt.expiresIn=30 ORDER BY pt.sales DESC LIMIT #{index},#{pageSize}")
    @Results({
            @Result(property = "banner",column = "banner",typeHandler = MyJSONArrayHandler.class),
            @Result(property = "rendering",column = "rendering",typeHandler = MyJSONArrayHandler.class)
    })
    public List<SequencePoster> getPosterListBySales(int cateId,int index,int pageSize);


    @Select("SELECT * FROM poster WHERE isEnabled=1 AND cateId=#{cateId} ORDER BY ${orderClause} LIMIT #{index},#{pageSize}")
    @Results({
            @Result(property = "banner",column = "banner",typeHandler = MyJSONArrayHandler.class),
            @Result(property = "rendering",column = "rendering",typeHandler = MyJSONArrayHandler.class)
    })
    public List<SequencePoster> getPosterListByClause(int cateId,String orderClause,int index,int pageSize);


    @Select("SELECT * FROM poster WHERE isEnabled=1 AND id=#{posterId}")
    @Results({
            @Result(property = "banner",column = "banner",typeHandler = MyJSONArrayHandler.class),
            @Result(property = "rendering",column = "rendering",typeHandler = MyJSONArrayHandler.class)
    })
    public Poster getPosterDetail(String posterId);
}