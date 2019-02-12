package com.amt.wechat.dao.poi;

import com.amt.wechat.model.poi.UserIncomeData;
import com.amt.wechat.model.poi.WXCodeData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-12 15:42
 * @version 1.0
 */
@Repository("poiUserWXCode")
@Mapper
public interface PoiUserWXCodeDao {


    @Insert("INSERT INTO poi_user_wxcode(userId, shareUrl,wxcodeUrl)VALUES(#{userId},#{shareUrl},#{wxcodeUrl})")
    public void addUserWXCodeData(WXCodeData data);


    @Select("SELECT wxcodeUrl FROM poi_user_wxcode WHERE userId=#{userId} AND shareUrl=#{url}")
    public String getWXCodeImgUrl(String userId,String url);


    @Select("SELECT COUNT(*)  FROM poi_user_income WHERE poiId=#{poiId}")
    public Integer countIncomeByPoiId(String poiId);

    @Insert("INSERT INTO poi_user_income(userId,poiId,inviteeId,SHARE,createDate)VALUES(#{userId},#{poiId},#{inviteeId},#{share},#{createDate})")
    public void addInvitation(UserIncomeData data);
}
