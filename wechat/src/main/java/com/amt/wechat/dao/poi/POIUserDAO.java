package com.amt.wechat.dao.poi;

import com.amt.wechat.model.poi.POIUserData;
import com.amt.wechat.model.poi.POIUserDataWX;
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

    @Select("SELECT * FROM poi_user_wx WHERE openid=#{openid}")
    public POIUserDataWX getUserDataWXByOpenid(String openid);

    @Insert("INSERT INTO poi_user_wx(wxId,authToken,gender, country, province, city, openid, unionid, nickName, avatarUrl, createDate) VALUE(#{wxId},#{authToken},#{gender}, #{country},#{province}, #{city}, #{openid}, #{unionid}, #{nickName}, #{avatarUrl}, #{createDate})")
    public void addShopUserWX(POIUserDataWX userDataWX);

    @Update("UPDATE poi_user_wx SET authToken = #{authToken}, gender = #{gender},country = #{country}, province=#{province}, city =#{city}, openid =#{openid}, unionid= #{unionid}, nickName = #{nickName},avatarUrl= #{avatarUrl}  WHERE wxId=#{wxId}")
    public void updateShopUserWX(POIUserDataWX data);

    @Select("SELECT * FROM poi_user WHERE mobile=#{mobile}")
    public POIUserData getUserDataByMobile(String mobile);

    @Insert("INSERT INTO poi_user(id, username, password,mobile, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled, isMaster, authToken, wxId,createDate) " +
            "VALUES(#{id},#{username},#{password},#{mobile},#{isAccountNonExpired},#{isAccountNonLocked},#{isCredentialsNonExpired},#{isEnabled},#{isMaster}, #{authToken},#{wxId}, #{createDate})")
    public void addShopUser(POIUserData shopUserData);


    public void updateShopUser(POIUserData shopUserData);
}