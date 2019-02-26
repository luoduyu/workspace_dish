package com.wmt.mgr.dao.wechat.bidding;

import com.wmt.mgr.model.bidding.BiddingPoiForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-26 15:13
 * @version 1.0
 */
@Repository("biddingDao")
@Mapper
public interface BiddingDao {

    @Select("<script>" +
            "SELECT poi.id,poi.name,poi.brandName,pu.name AS masterName,pu.mobile AS masterMobile,pa.curBiddingBalance " +
            "FROM poi,poi_user pu,poi_account pa " +
            "WHERE poi.id=pu.poiId AND poi.id=pa.poiId " +
            "<if test=\"brandName !=null and brandName.length()!=0 \"> AND poi.brandName LIKE '%${brandName}%' </if>" +
            "<if test=\"masterName !=null and masterName.length() !=0 \"> AND pu.name LIKE '%${masterName}%' </if>" +
            "<if test=\"masterMobile !=null and masterMobile.length() !=0 \"> AND pu.mobile=#{masterMobile} </if>" +
            "ORDER BY poi.createTime DESC LIMIT #{index},#{pageSize}"+
            "</script>")
    public List<BiddingPoiForm> findPoiDataList(@Param("brandName") String brandName,@Param("masterName")String masterName,@Param("masterMobile")String masterMobile,@Param("index")int index,@Param("pageSize")int pageSize);


    @Select("<script>" +
            "SELECT COUNT(*) " +
            "FROM poi,poi_user pu,poi_account pa " +
            "WHERE poi.id=pu.poiId AND poi.id=pa.poiId " +
            "<if test=\"brandName !=null and brandName.length()!=0 \"> AND poi.brandName LIKE '%${brandName}%' </if>" +
            "<if test=\"masterName !=null and masterName.length()!=0 \"> AND pu.name LIKE '%${masterName}%' </if>" +
            "<if test=\"masterMobile !=null and masterMobile.length()!=0 \"> AND pu.mobile=#{masterMobile}</if>"+
            "</script>")
    public Integer countPoiData(@Param("brandName") String brandName, @Param("masterName")String masterName, @Param("masterMobile")String masterMobile);
}
