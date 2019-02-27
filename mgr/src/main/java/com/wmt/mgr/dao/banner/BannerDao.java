package com.wmt.mgr.dao.banner;

import com.wmt.mgr.model.banner.BannerData;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-27 17:19
 * @version 1.0
 */
@Repository("bannerDao")
@Mapper
public interface BannerDao {


    @Insert("INSERT INTO home_banners (name, isEnabled, showSeq, imgUrl, expiredStart, expiredEnd, createDate)VALUES(#{name},#{isEnabled},#{showSeq},#{imgUrl},#{expiredStart},#{expiredEnd},#{createDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public void addBannerData(BannerData bannerData);


    @Delete("DELETE FROM home_banners WHERE id =#{id}")
    public void bannerRM(int id);

    @Update("UPDATE home_banners SET name=#{name},isEnabled=#{isEnabled},showSeq=#{showSeq},imgUrl=#{imgUrl},expiredStart=#{expiredStart},expiredEnd =#{expiredEnd} where id=#{id}")
    public void updateBannerData(BannerData bannerData);


    @Select("SELECT MAX(showSeq) FROM home_banners")
    public Integer getBannerMaxShowSeq();


    @Select("SELECT * FROM home_banners WHERE id=#{id} LIMIT 1")
    public BannerData getBannerData(int id);

    @Select("<script>" +
            "SELECT COUNT(*) FROM home_banners WHERE 1=1" +
            "<if test=\"name != null and name.length() !=0 \"> AND name LIKE '%${name}%'</if>"+
            "<if test=\"isEnabled != -1\"> AND isEnabled=#{isEnabled}</if>"+
            "<if test=\"expiredStart != null and expiredStart.length() != 0 \"> AND expiredStart &gt;= '#{expiredStart}' </if>"+
            "<if test=\"expiredEnd != null and expiredEnd.length() != 0 \"> AND expiredEnd &lt;= '#{expiredEnd}' </if>"+
            "<if test=\"createDateStart != null and createDateStart.length() != 0\"> AND createDate &gt;= '#{createDateStart}' </if>"+
            "<if test=\"createDateEnd != null and createDateEnd.length() != 0 \"> AND createDate &lt;= '#{createDateEnd}' </if>"+
            " ORDER BY createDate DESC" +
            "</script>")
    public Integer countBannerData(String name,int isEnabled,String expiredStart,String expiredEnd,String createDateStart,String createDateEnd);


    @Select("<script>" +
            "SELECT * FROM home_banners WHERE 1=1" +
            "<if test=\"name != null and name.length() !=0 \"> AND name LIKE '%${name}%'</if>"+
            "<if test=\"isEnabled != -1\"> AND isEnabled=#{isEnabled}</if>"+
            "<if test=\"expiredStart != null and expiredStart.length() != 0 \"> AND expiredStart &gt;= '#{expiredStart}' </if>"+
            "<if test=\"expiredEnd != null and expiredEnd.length() != 0 \"> AND expiredEnd &lt;= '#{expiredEnd}' </if>"+
            "<if test=\"createDateStart != null and createDateStart.length() != 0\"> AND createDate &gt;= '#{createDateStart}' </if>"+
            "<if test=\"createDateEnd != null and createDateEnd.length() != 0 \"> AND createDate &lt;= '#{createDateEnd}' </if>"+
            " ORDER BY createDate DESC LIMIT #{index},#{pageSize}" +
            "</script>")
    public List<BannerData> getBannerDataList(String name,int isEnabled,String expiredStart,String expiredEnd,String createDateStart,String createDateEnd,int index,int pageSize);
}
