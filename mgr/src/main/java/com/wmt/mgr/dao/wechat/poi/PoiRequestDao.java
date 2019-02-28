package com.wmt.mgr.dao.wechat.poi;

import com.wmt.mgr.model.poi.PoiRequestData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 * 店铺申请数据持久层
 *
 * @author lujunp Create on 2019/2/27 17:44
 * @version 1.0
 */
@Repository("poiRequestDao")
@Mapper
public interface PoiRequestDao {

    @Select("<script>" +
            "SELECT * FROM poi_request WHERE 1=1 " +
            "<if test=\"id != null and id != 0\"> and id = #{id} </if>" +
            "<if test=\"poiUserId != null and poiUserId.length()!=0 \"> and poiUserId = #{poiUserId} </if>" +
            "<if test=\"brandName != null and brandName.length() != 0\"> and brandName LIKE '%${brandName}%' </if>" +
            "<if test=\"province != null and province.length() != 0\"> and province LIKE '%${province}%' </if>" +
            "<if test=\"city != null and city.length() != 0\"> and city LIKE '%${city}%' </if>" +
            "<if test=\"districts != null and districts.length() != 0\"> and districts LIKE '%${districts}%' </if>" +
            "<if test=\"startTime != null and startTime.length() != 0\"> and createTime &gt;#{startTime}</if>" +
            "<if test=\"endTime != null and endTime.length() != 0\"> and  createTime &lt;#{endTime}</if>" +
            "ORDER BY createTime DESC LIMIT #{index},#{pageSize}" +
            "</script>")
    public List<PoiRequestData> selectPoiRequestList(Integer id, String poiUserId,
                                                     String brandName, String province,
                                                     String city, String districts, String startTime,
                                                     String endTime, int index, int pageSize);


    @Select("SELECT * FROM poi_request WHERE id=#{id}")
    public PoiRequestData selectPoiRequestDetail(int id);

}
