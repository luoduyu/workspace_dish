package com.amt.wechat.dao.poi;

import com.amt.wechat.model.poi.PoiData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  门店数据
 *
 * @author adu Create on 2019-01-03 18:28
 * @version 1.0
 */
@Repository("poiDao")
@Mapper
public interface PoiDao {

    @Insert("INSERT INTO poi(id,name,contry,province,city,districts,street,address,brandName,cateId,accountName,accountPassword, mtAppAuthToken,eleShopId,createTime,updTime)VALUES(#{id},#{name},#{contry},#{province},#{city},#{districts},#{street},#{address},#{brandName},#{cateId},#{accountName},#{accountPassword},#{mtAppAuthToken},#{eleShopId},#{createTime}, #{updTime})")
    public void addPoiData(PoiData poiData);


    @Select("SELECT * FROM poi WHERE id=#{id}")
    public PoiData getPoiData(String id);


    @Update("UPDATE poi SET contry=#{contry},province=#{province},city =#{city},districts =#{districts},street =#{street},address =#{address},brandName =#{brandName},cateId=#{cateId},updTime=#{updTime} WHERE id = #{id}")
    public void updatePoiData(PoiData poiData);
}
