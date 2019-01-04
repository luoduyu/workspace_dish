package com.amt.wechat.dao.poi;

import com.amt.wechat.model.poi.PoiUserData;
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
@Repository("poiUserDao")
@Mapper
public interface PoiUserDao {


    @Insert("INSERT INTO poi_user (id, accessToken, isAccountNonExpired,isAccountNonLocked, isCredentialsNonExpired, isEnabled,isMaster, password, mobile, gender, countryCode, province, city, openid, unionid, name,nickName, avatarUrl, createTime,updTime) VALUES(#{id}, #{accessToken}, #{isAccountNonExpired},#{isAccountNonLocked}, #{isCredentialsNonExpired}, #{isEnabled},#{isMaster},#{password}, #{mobile}, #{gender}, #{countryCode}, #{province},#{city}, #{openid}, #{unionid},#{name},#{nickName}, #{avatarUrl}, #{createTime},#{updTime})")
    public void addPOIUser(PoiUserData poiUserData);

    @Update("UPDATE poi_user SET accessToken = #{accessToken}, gender = #{gender},countryCode = #{countryCode}, province=#{province}, city =#{city}, openid =#{openid}, unionid= #{unionid}, nickName = #{nickName},avatarUrl= #{avatarUrl}  WHERE id=#{id}")
    public void updatePOIUser(PoiUserData poiUserData);

    @Update("UPDATE poi_user SET name = #{name}, mobile = #{mobile} WHERE id=#{id}")
    public void updatePOIUserNameAndMobile(String name,String mobile,String id);

    @Update("UPDATE poi_user SET mobile = #{mobile} WHERE id=#{id}")
    public void updatePOIUserMobile(String mobile,String id);


    @Update("UPDATE poi_user SET name = #{name} WHERE id=#{id}")
    public void updatePOIUserName(String name ,String id);

    @Select("SELECT * FROM poi_user WHERE openid=#{openid}")
    public PoiUserData getPOIUserDataByOpenid(String openid);

    @Select("SELECT * FROM poi_user WHERE mobile=#{mobile}")
    public PoiUserData getPOIUserDataByMobile(String mobile);

    @Select("SELECT * FROM poi_user WHERE id=#{id}")
    public PoiUserData getPOIUserDataById(String id);


    @Select("SELECT * FROM poi_user WHERE openid=#{openid} OR mobile=#{mobile}")
    public PoiUserData getPOIUserData(String openid,String mobile);
}