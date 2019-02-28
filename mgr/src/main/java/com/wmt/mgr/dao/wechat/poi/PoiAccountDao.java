package com.wmt.mgr.dao.wechat.poi;

import com.wmt.mgr.model.poi.PoiAccountForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 * 商户账户余额数据持久层
 *
 * @author lujunp Create on 2019/2/28 13:54
 * @version 1.0
 */
@Repository("poiAccountDao")
@Mapper
public interface PoiAccountDao {

    @Select("<script>" +
            "SELECT poi.id,poi.name,poi.brandName,pu.name AS poiUserName,pu.mobile AS poiUserMobile,pa.curBalance AS accountTotal, pa.curRedBalance AS presentBalance " +
            "FROM poi,poi_user pu,poi_account pa " +
            "WHERE poi.id=pu.poiId AND poi.id=pa.poiId " +
            "<if test=\"brandName !=null and brandName.length()!=0 \"> AND poi.brandName LIKE '%${brandName}%' </if>" +
            "<if test=\"poiUserName !=null and poiUserName.length() !=0 \"> AND pu.name LIKE '%${poiUserName}%' </if>" +
            "<if test=\"poiUserMobile !=null and poiUserMobile.length() !=0 \"> AND pu.mobile=#{poiUserMobile} </if>" +
            "ORDER BY poi.createTime DESC LIMIT #{index},#{pageSize}"+
            "</script>")
    public List<PoiAccountForm> findPoiAccountList(@Param("brandName") String brandName,
                                                   @Param("poiUserName")String poiUserName,
                                                   @Param("poiUserMobile")String poiUserMobile,
                                                   @Param("index")int index,
                                                   @Param("pageSize")int pageSize);


    @Select("<script>" +
            "SELECT COUNT(*)" +
            "FROM poi,poi_user pu,poi_account pa " +
            "WHERE poi.id=pu.poiId AND poi.id=pa.poiId " +
            "<if test=\"brandName !=null and brandName.length()!=0 \"> AND poi.brandName LIKE '%${brandName}%' </if>" +
            "<if test=\"poiUserName !=null and poiUserName.length() !=0 \"> AND pu.name LIKE '%${poiUserName}%' </if>" +
            "<if test=\"poiUserMobile !=null and poiUserMobile.length() !=0 \"> AND pu.mobile=#{poiUserMobile} </if>" +
            "</script>")
    public Integer countPoiAccount(@Param("brandName") String brandName,
                                   @Param("poiUserName")String masterName,
                                   @Param("poiUserMobile")String masterMobile);


}
