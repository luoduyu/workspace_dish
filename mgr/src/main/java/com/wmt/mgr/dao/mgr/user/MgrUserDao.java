package com.wmt.mgr.dao.mgr.user;

import com.wmt.mgr.model.mgr.user.MgrUserData;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-22 11:50
 * @version 1.0
 */

@Repository("mgrUserDao")
@Mapper
public interface MgrUserDao {

    @Insert("INSERT mgr_user(accessToken,isAccountNonExpired,isAccountNonLocked,isCredentialsNonExpired,isEnabled, password,mobile,gender,name,nickName,createTime,updTime) " +
            "VALUES(#{accessToken},#{isAccountNonExpired},#{isAccountNonLocked},#{isCredentialsNonExpired},#{isEnabled},#{password},#{mobile},#{gender},#{name},#{nickName},#{createTime},#{updTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public void addMgrUser(MgrUserData mgrUserData);


    @Update("UPDATE mgr_user SET isEnabled=#{isEnabled},password=#{password},mobile=#{mobile},name =#{name},updTime = #{updTime} WHERE id = #{id}")
    public void updateMgrUser(MgrUserData mgrUserData);


    @Update("UPDATE mgr_user SET accessToken = #{accessToken}, updTime=#{updTime}  WHERE id=#{id}")
    public void updateMgrUser4Login(MgrUserData mgrUserData);

    @Select("SELECT * FROM mgr_user WHERE id=#{id}")
    public MgrUserData getMgrUserDataById(int id);


    @Select("SELECT * FROM mgr_user WHERE mobile=#{mobile}")
    public MgrUserData getMgrUserDataByMobile(String mobile);


    @Select("<script>" +
            "SELECT * FROM mgr_user WHERE 1=1 " +
            "<if test=\"name != null and name.length() != 0 \"> and name = #{name} </if>"+
            "<if test=\"mobile != null and mobile.length()!=0 \"> and mobile = #{mobile} </if>" +
            "<if test=\"isEnabled != null\"> and isEnabled = #{isEnabled} </if>" +
            "ORDER BY updTime DESC LIMIT #{index},#{pageSize}" +
            "</script>")
    public List<MgrUserData> getMgrUserDataList(String name, String mobile, Integer isEnabled, int index, int pageSize);


    /**
     * name != null and name.trim().length() !=0 等价于 name != null and name.trim.length !=0
     * 只能用 and ,不能用 '&&'
     *
     * 错误:<if test="takeWay == '1' and workday != null ">
     * 正确:<if test='takeWay == "1" and workday != null '>
     *     <if test="takeWay == '1'.toString() and workday != null ">
     */
    @Select("<script>" +
            "SELECT count(id) FROM mgr_user WHERE 1=1 " +
            "<if test=\"name != null and name.length()!=0 \"> and name = #{name} </if>"+
            "<if test=\"mobile != null and mobile.length()!=0 \"> and mobile = #{mobile} </if>" +
            "<if test=\"isEnabled != null\"> and isEnabled = #{isEnabled} </if>" +
            "</script>")
    public Integer countUserData(String name, String mobile, Integer isEnabled);
}
