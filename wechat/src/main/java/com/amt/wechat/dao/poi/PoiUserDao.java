package com.amt.wechat.dao.poi;

import com.amt.wechat.model.poi.PoiCandidate;
import com.amt.wechat.model.poi.PoiUserData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author Administrator Create on 2018-12-17 09:53
 * @version 1.0
 */
@Repository("poiUserDao")
@Mapper
public interface PoiUserDao {

    @Insert("INSERT INTO poi_user (id, accessToken, isAccountNonExpired,isAccountNonLocked, isCredentialsNonExpired, isEnabled,isMaster,shareBalance,password, mobile, gender, countryCode, province, city, openid, unionid, name,nickName, avatarUrl,inviterId, createTime,updTime) VALUES(#{id}, #{accessToken}, #{isAccountNonExpired},#{isAccountNonLocked}, #{isCredentialsNonExpired}, #{isEnabled},#{isMaster},#{shareBalance},#{password}, #{mobile}, #{gender}, #{countryCode}, #{province},#{city}, #{openid}, #{unionid},#{name},#{nickName}, #{avatarUrl},#{inviterId}, #{createTime},#{updTime})")
    public void addPOIUser(PoiUserData poiUserData);

    @Update("UPDATE poi_user SET accessToken = #{accessToken}, gender = #{gender},countryCode = #{countryCode}, province=#{province}, city =#{city}, openid =#{openid}, unionid= #{unionid}, nickName = #{nickName},avatarUrl= #{avatarUrl},updTime=#{updTime}  WHERE id=#{id}")
    public void updatePOIUser(PoiUserData poiUserData);

    @Update("UPDATE poi_user SET accessToken=#{accessToken},updTime=#{updTime} WHERE id=#{id}")
    public void updateUserAccessToken(String accessToken,String updTime,String id);

    @Update("UPDATE poi_user SET poiId=#{poiId},updTime=#{updTime} WHERE id=#{id}")
    public void updateUserPoiId(String poiId,String updTime,String id);


    @Update("UPDATE poi_user SET isMaster=#{isMaster},name = #{name}, mobile = #{mobile},updTime=#{updTime} WHERE id=#{id}")
    public void update4AuthBoss(int isMaster,String name,String mobile,String updTime,String id);

    @Update("UPDATE poi_user SET name = #{name}, mobile = #{mobile},updTime=#{updTime} WHERE id=#{id}")
    public void employeeSet(String name,String mobile,String updTime,String id);


    @Update("UPDATE poi_user SET isMaster=#{isMaster},poiId=#{poiId},updTime=#{updTime} WHERE id=#{id}")
    public void update4PoiBind(int isMaster,String poiId,String updTime,String id);


    @Update("UPDATE poi_user SET mobile = #{mobile},updTime=#{updTime} WHERE id=#{id}")
    public void updatePOIUserMobile(String mobile,String updTime,String id);

    @Update("UPDATE poi_user SET isMaster = #{isMaster},updTime=#{updTime} WHERE id=#{id}")
    public void updatePoiUserMaster(int isMaster,String updTime,String id);

    @Update("UPDATE poi_user SET isMaster = #{isMaster},poiId=#{poiId},updTime=#{updTime} WHERE id=#{id}")
    public void removeUserFomPOI(int isMaster,String poiId,String updTime,String id);

    @Update("UPDATE poi_user SET name = #{name},updTime=#{updTime} WHERE id=#{id}")
    public void updatePOIUserName(String name ,String updTime,String id);


    @Select("SELECT * FROM poi_user WHERE openid=#{openid}")
    public PoiUserData getPOIUserDataByOpenid(String openid);

    @Select("SELECT * FROM poi_user WHERE mobile=#{mobile}")
    public PoiUserData getPOIUserDataByMobile(String mobile);

    @Select("SELECT * FROM poi_user WHERE mobile=#{mobile} AND id != #{excludeId}")
    public PoiUserData getPOIUserDataByMobileExcludeId(String mobile,String excludeId);

    @Select("SELECT * FROM poi_user WHERE id=#{id}")
    public PoiUserData getPOIUserDataById(String id);

    @Select("SELECT * FROM poi_user WHERE poiId=#{poiId} LIMIT 200")
    public List<PoiUserData> getPoiEmployeeList(String poiId);


    @Select("SELECT mobile FROM poi_user WHERE poiId =#{poiId} AND isMaster=#{isMaster} limit 1")
    public String getMasterMobile(String poiId,int isMaster);

    @Select("SELECT inviterId FROM poi_user WHERE id =#{id} limit 1")
    public String getInviterId(String id);

    @Update("UPDATE poi_user SET shareBalance = shareBalance + #{plusBalance} WHERE id=#{id}")
    public void updateShareBlance(String id,int plusBalance);





    @Insert("INSERT INTO poi_user_candidate(poiName,poiId,mobile,createTime, userId)VALUES(#{poiName},#{poiId},#{mobile},#{createTime},#{userId})")
    public void addInvite(PoiCandidate data);

    @Select("DELETE FROM poi_user_candidate WHERE id=#{id}")
    public void removeInvoteById(int id);


    @Select("SELECT * FROM poi_user_candidate WHERE mobile=#{mobile} AND poiId=#{poiId} LIMIT 1")
    public PoiCandidate getPoiCandidate(String mobile,String poiId);

    @Select("SELECT * FROM poi_user_candidate WHERE mobile=#{mobile}")
    public List<PoiCandidate> getPoiCandidateList(String mobile);

    @Select("SELECT * FROM poi_user_candidate WHERE id=#{id} LIMIT 1")
    public PoiCandidate getPoiCandidateById(int id);
}