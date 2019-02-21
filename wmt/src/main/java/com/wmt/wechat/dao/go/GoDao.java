package com.wmt.wechat.dao.go;

import com.wmt.wechat.model.go.GOData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  运营类DAO
 *
 * @author adu Create on 2019-01-02 19:36
 * @version 1.0
 */
@Repository("goDao")
@Mapper
public interface GoDao {

    @Insert("INSERT INTO poi_request(usefor,brandName, province, city, address, districts, platform, poiType, dishCateId, amount, poiId,poiUserId,auditStatus,auditDate,opinion,createTime,updTime) VALUES(#{usefor},#{brandName},#{province}, #{city}, #{address},#{districts},#{platform},#{poiType},#{dishCateId},#{amount},#{poiId},#{poiUserId}, #{auditStatus}, #{auditDate},#{opinion}, #{createTime},#{updTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public void addRequestForm(GOData goData);

    @Select("SELECT * FROM poi_request WHERE poiId=#{poiId} AND usefor=#{usefor} LIMIT 1")
    public GOData getDataByPOIId(String poiId,int usefor);


    @Select("SELECT * FROM poi_request WHERE id=#{id}")
    public GOData getDataById(int id);


    @Select("UPDATE poi_request SET poiUserId=#{poiUserId},auditStatus = 0 ,updTime=#{updTime} WHERE id =#{id}")
    public void updateData(String poiUserId,String updTime,int id);
}