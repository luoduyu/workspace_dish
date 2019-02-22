package com.wmt.mgr.dao.user;

import com.wmt.mgr.model.user.MgrUserData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-22 11:50
 * @version 1.0
 */

@Repository("mgrUserDao")
@Mapper
public interface MgrUserDao {

    @Select("SELECT * FROM mgr_user WHERE id=#{id}")
    public MgrUserData getMgrUserDataById(int id);


    @Select("SELECT * FROM mgr_user WHERE mobile=#{mobile}")
    public MgrUserData getMgrUserDataByMobile(String mobile);


    @Update("UPDATE mgr_user SET accessToken = #{accessToken}, updTime=#{updTime}  WHERE id=#{id}")
    public void updateMgrUser4Login(MgrUserData mgrUserData);
}
