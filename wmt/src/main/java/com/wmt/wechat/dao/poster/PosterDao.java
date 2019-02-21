package com.wmt.wechat.dao.poster;

import com.wmt.wechat.model.poster.PosterData;
import com.wmt.wechat.model.poster.RecommendPoster;
import com.wmt.wechat.model.poster.SequencePoster;
import com.wmt.wechat.model.poster.TopPosterData;
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

@Repository("posterDao")
@Mapper
public interface PosterDao {

//    @Select("SELECT * FROM poster WHERE isEnabled=1 ORDER BY updTime DESC LIMIT #{index},#{pageSize}")
//    @Results({
//            @Result(property = "rendering", column = "rendering",typeHandler = MyJSONArrayHandler.class)
//    })

    @Select("SELECT * FROM poster WHERE isEnabled=1 ORDER BY updTime DESC LIMIT #{index},#{pageSize}")
    public List<SequencePoster> getPosterList(int index, int pageSize);

    @Select("SELECT COUNT(*) FROM poster WHERE isEnabled=1")
    public int countPosterSize();



    @Select("SELECT * FROM poster WHERE id=#{id}")
    public PosterData getPosterData(int id);

    @Select("SELECT * FROM poster WHERE id IN(${ids})")
    @MapKey("id")
    public Map<Integer,PosterData> getPosterDataMap(@Param("ids") String ids);





//
//    @Select("SELECT * FROM poster WHERE isEnabled=1 AND id IN (${ids})")
//    @Results({
//            @Result(property = "rendering",column = "rendering",typeHandler = MyJSONArrayHandler.class)
//    })

    @Select("SELECT * FROM poster WHERE isEnabled=1 AND id IN (${ids})")
    public List<SequencePoster> getPosterListByIds(@Param("ids")String ids);


//    @Insert("INSERT INTO poster (platform,cateId, name,coverImg,rendering, memberPrice,price, createTime, updTime)" +
//            "VALUES(#{platform},#{cateId},#{name},#{coverImg},#{rendering,jdbcType=OTHER,typeHandler=MyJSONArrayHandler},#{memberPrice},#{price},#{createTime},#{updTime})")

    @Insert("INSERT INTO poster (platform,cateId, name,coverImg,rendering, memberPrice,price, createTime, updTime)" +
            "VALUES(#{platform},#{cateId},#{name},#{coverImg},#{rendering},#{memberPrice},#{price},#{createTime},#{updTime})")
    public void addPoster(PosterData posterData);



    @Select("SELECT posterId,showSeq FROM poster_recommend ORDER BY showSeq ASC LIMIT 500")
    @MapKey("posterId")
    public Map<Integer, RecommendPoster> getRecommendPoster();


    /**
     * 最近一个月/周/天销量最高的8-10条
     *
     * @param timeUnit 期限单位;1:"DAYS"(天),2:"HOURS"(小时)
     * @param expiresIn 1:最近一个月/周/天;2:最近两个月/周/天;3:最近三个月/周/天
     * @param pageSize 页大小
     * @return
     */
    @Select("SELECT posterId,sales FROM poster_sales_top WHERE timeUnit=#{timeUnit} AND expiresIn=#{expiresIn} ORDER BY sales DESC LIMIT #{pageSize}")
    @MapKey("posterId")
    public Map<Integer, TopPosterData> getTopPoster(int timeUnit, int expiresIn, int  pageSize);


    /**
     * 分类别的、按销量倒序排列的海报列表
     * @return
     */
//    @Select("SELECT p.*,pt.sales AS showSeq FROM poster p LEFT JOIN poster_sales_top pt ON p.id=pt.posterId WHERE p.isEnabled=1 AND p.cateId = #{cateId} AND pt.timeUnit=1 AND pt.expiresIn=30 ORDER BY pt.sales DESC LIMIT #{index},#{pageSize}")
//    @Results({
//            @Result(property = "rendering",column = "rendering",typeHandler = MyJSONArrayHandler.class)
//    })

    @Select("SELECT p.*,pt.sales AS showSeq FROM poster p LEFT JOIN poster_sales_top pt ON p.id=pt.posterId WHERE p.isEnabled=1 AND p.cateId = #{cateId} AND pt.timeUnit=1 AND pt.expiresIn=30 ORDER BY pt.sales DESC LIMIT #{index},#{pageSize}")
    public List<SequencePoster> getPosterListBySales(int cateId,int index,int pageSize);


    @Select("SELECT COUNT(*) FROM poster p LEFT JOIN poster_sales_top pt ON p.id=pt.posterId WHERE p.isEnabled=1 AND p.cateId = #{cateId} AND pt.timeUnit=1 AND pt.expiresIn=30")
    public int countPosterListBySales(int cateId);


//    @Select("SELECT * FROM poster WHERE isEnabled=1 AND cateId=#{cateId} ORDER BY ${orderClause} LIMIT #{index},#{pageSize}")
//    @Results({
//            @Result(property = "rendering",column = "rendering",typeHandler = MyJSONArrayHandler.class)
//    })

    @Select("SELECT * FROM poster WHERE isEnabled=1 AND cateId=#{cateId} ORDER BY ${orderClause} LIMIT #{index},#{pageSize}")
    public List<SequencePoster> getPosterListByClause(int cateId,String orderClause,int index,int pageSize);

    @Select("SELECT count(*) FROM poster WHERE isEnabled=1 AND cateId=#{cateId}")
    public int  countPosterListByClause(int cateId);


//    @Select("SELECT * FROM poster WHERE isEnabled=1 AND id=#{posterId}")
//    @Results({
//            @Result(property = "rendering",column = "rendering",typeHandler = MyJSONArrayHandler.class)
//    })

    @Select("SELECT * FROM poster WHERE isEnabled=1 AND id=#{posterId}")
    public PosterData getPosterDetail(int posterId);
}