package com.wmt.wechat.dao.poi;

import com.wmt.wechat.model.poi.PoiData;
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

    @Insert("INSERT INTO poi(id,name,country,province,city,districts,street,address,brandName,cateId,logoImg,accountName,accountPassword, mtAppAuthToken,eleShopId,balancePwd,balancePwdFree,createTime,updTime)VALUES(#{id},#{name},#{country},#{province},#{city},#{districts},#{street},#{address},#{brandName},#{cateId},#{logoImg},#{accountName},#{accountPassword},#{mtAppAuthToken},#{eleShopId},#{balancePwd},#{balancePwdFree},#{createTime}, #{updTime})")
    public void addPoiData(PoiData poiData);

    @Select("SELECT * FROM poi WHERE id=#{id} LIMIT 1")
    public PoiData getPoiData(String id);

    @Update("UPDATE poi SET country=#{country},province=#{province},city =#{city},districts =#{districts},street =#{street},address =#{address},brandName =#{brandName},cateId=#{cateId},updTime=#{updTime} WHERE id = #{id}")
    public void updatePoiData(PoiData poiData);

    @Update("UPDATE poi SET balancePwd=#{pwd} WHERE id=#{poiId}")
    public void updateBalancePwd(String pwd,String poiId);


    @Update("UPDATE poi SET balancePwdFree=#{balancePwdFree} WHERE id=#{poiId}")
    public void updateBalancePwdRequired(int balancePwdFree,String poiId);

    @Update("UPDATE poi SET eleShopId=#{eleShopId},accountName=#{accountName},accountPassword=#{accountPassword}, updTime=#{updTime} WHERE id = #{id}")
    public void eleAuth(PoiData poiData);

    @Update("UPDATE poi SET mtAppAuthToken=#{mtAppAuthToken},accountName=#{accountName},accountPassword=#{accountPassword}, updTime=#{updTime} WHERE id = #{id}")
    public void mtAuth(PoiData poiData);
}