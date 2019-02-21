package com.wmt.wechat.dao.poi;

import com.wmt.wechat.model.poi.UserIncomeData;
import com.wmt.wechat.model.poi.WXCodeData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-12 15:42
 * @version 1.0
 */
@Repository("poiUserWXCode")
@Mapper
public interface PoiUserWXCodeDao {


    @Insert("INSERT INTO poi_user_wxcode(userId, shareUrl,wxcodeUrl,expireDate)VALUES(#{userId},#{shareUrl},#{wxcodeUrl},#{expireDate})")
    public void addUserWXCodeData(WXCodeData data);


    @Update("UPDATE poi_user_wxcode SET wxcodeUrl=#{wxcodeUrl},expireDate=#{expireDate} WHERE userId=#{userId} AND shareUrl=#{shareUrl}")
    public void updateUserWXCodeData(WXCodeData data);


    @Select("SELECT * FROM poi_user_wxcode WHERE userId=#{userId} AND shareUrl=#{url}")
    public WXCodeData getWXCodeImgUrl(String userId,String url);


    @Select("SELECT COUNT(*)  FROM poi_user_income WHERE poiId=#{poiId}")
    public Integer countIncomeByPoiId(String poiId);

    @Insert("INSERT INTO poi_user_income(userId,userAvatarUrl,userNickName,poiId,inviteeId,avatarUrl,nickName,`share`,durationUnit,duration,createDate)VALUES(#{userId},#{userAvatarUrl},#{userNickName},#{poiId},#{inviteeId},#{avatarUrl},#{nickName},#{share},#{durationUnit},#{duration},#{createDate})")
    public void addInvitationIncome(UserIncomeData data);

    /**
     * 某一天邀请数
     * @param yyyyMMdd
     * @param userId
     * @return
     */
    @Select("SELECT COUNT(*) FROM poi_user_income WHERE userId=#{userId} AND DATE(createDate)=#{yyyyMMdd}")
    public Integer countInviteNum4Date(String yyyyMMdd,String userId);


    /**
     * 某一天邀请数据
     * @param yyyyMMdd
     * @param userId
     * @param index
     * @param pageSize
     * @return
     */
    @Select("SELECT * FROM poi_user_income WHERE userId=#{userId} AND DATE(createDate)=#{yyyyMMdd} ORDER BY createDate DESC LIMIT #{index},#{pageSize}")
    public List<UserIncomeData> getInviteList4Date(String yyyyMMdd, String userId,int index,int pageSize);

    /**
     * 历史累计邀请数
     * @param userId
     * @return
     */
    @Select("SELECT COUNT(*) FROM poi_user_income WHERE userId=#{userId}")
    public Integer countInviteNum4ALL(String userId);

    @Select("SELECT * FROM poi_user_income WHERE userId=#{userId} ORDER BY createDate DESC LIMIT #{index},#{pageSize}")
    public List<UserIncomeData> getInviteList4ALL(String userId,int index,int pageSize);

    /**
     * 累计赚取
     * @param userId
     * @return
     */
    @Select("SELECT SUM(SHARE)  FROM poi_user_income WHERE userId=#{userId}")
    public Long sumInviteShare(String userId);


    /**
     * 排行榜
     * @param limit
     * @return
     */
    @Select("SELECT	* FROM  poi_share_top ORDER BY totalShare DESC LIMIT #{limit}")
    public List<InviteTop> getTopInviteIncome(int limit);
}