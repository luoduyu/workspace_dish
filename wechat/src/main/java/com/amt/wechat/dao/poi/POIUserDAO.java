package com.amt.wechat.dao.poi;

import com.amt.wechat.model.poi.POIUserData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author Administrator Create on 2018-12-17 09:53
 * @version 1.0
 */
@Repository("poiUserDAO")
@Mapper
public interface POIUserDAO {


    @Insert("INSERT INTO poi_user (id, authToken, isAccountNonExpired,isAccountNonLocked, isCredentialsNonExpired, isEnabled,isMaster, username, PASSWORD, mobile, gender, countryCode, province, city, openid, unionid, nickName, avatarUrl, cTime,uTime) VALUES(#{id}, #{authToken}, #{isAccountNonExpired},#{isAccountNonLocked}, #{isCredentialsNonExpired}, #{isEnabled},#{isMaster}, #{username}, #{password}, #{mobile}, #{gender}, #{countryCode}, #{province},#{city}, #{openid}, #{unionid}, #{nickName}, #{avatarUrl}, #{cTime},#{uTime})")
    public void addPOIUser(POIUserData poiUserData);

    @Update("UPDATE poi_user_wx SET authToken = #{authToken}, gender = #{gender},country = #{country}, province=#{province}, city =#{city}, openid =#{openid}, unionid= #{unionid}, nickName = #{nickName},avatarUrl= #{avatarUrl}  WHERE id=#{id}")
    public void updatePOIUser(POIUserData poiUserData);

    @Select("SELECT * FROM poi_user_wx WHERE openid=#{openid}")
    public POIUserData getPOIUserDataByOpenid(String openid);

    @Select("SELECT * FROM poi_user WHERE mobile=#{mobile}")
    public POIUserData getPOIUserDataByMobile(String mobile);

    @Select("SELECT * FROM poi_user WHERE openid=#{openid} OR mobile=#{mobile}")
    public POIUserData getPOIUserData(String openid,String mobile);
}