package com.wmt.mgr.dao.wechat.poi;

import com.wmt.mgr.model.poi.PoiUserData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Copyright (c) 2019 by CANSHU
 * 店铺人员持久层接口
 *
 * @author lujunp Create on 2019/2/27 18:09
 * @version 1.0
 */
@Repository("poiUserDao")
@Mapper
public interface PoiUserDao {

    @Select("<script>" +
            "SELECT * FROM poi_user WHERE 1=1 " +
            "<if test=\"poiUserName != null and poiUserName.length() != 0 \"> and name = #{poiUserName} </if>" +
            "<if test=\"poiUserMobile != null and poiUserMobile.length() !=0 \"> and mobile = #{poiUserMobile} </if>" +
            "</script>")
    public PoiUserData selectPoiUser(String poiUserName,String poiUserMobile);

    @Select("SELECT * FROM poi_user WHERE id=#{id}")
    public PoiUserData selectPoiUserById(String id);

    @Select("SELECT * FROM poi_user WHERE mobile=#{mobile}")
    public PoiUserData getPOIUserDataByMobile(String mobile);

}
