package com.wmt.mgr.dao.wechat.member;

import com.wmt.mgr.model.member.PoiMemberForm;
import com.wmt.mgr.model.member.PoiMemberRDData;
import com.wmt.mgr.model.member.PoizMemberList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 * 会员订单持久层接口
 *
 * @author lujunp Create on 2019/2/27 13:54
 * @version 1.0
 */
@Repository("poiMemberDao")
@Mapper
public interface PoiMemberDao {

    @Select("<script>" +
            "SELECT * FROM poi_member_rd WHERE 1=1 " +
            "<if test=\"orderId != null and orderId.length() != 0 \"> and orderId = #{orderId} </if>" +
            "<if test=\"userMobile != null and userMobile.length()!=0 \"> and userMobile = #{userMobile} </if>" +
            "<if test=\"poiName != null and poiName.length() != 0\"> and poiName LIKE '%${poiName}%' </if>" +
            "<if test=\"startTime != null and startTime.length() != 0\"> and timeEnd &gt;#{startTime}</if>" +
            "<if test=\"endTime != null and endTime.length() != 0\"> and  timeEnd &lt;#{endTime}</if>" +
            "ORDER BY timeEnd DESC LIMIT #{index},#{pageSize}" +
            "</script>")
    public List<PoiMemberRDData> getPoiMemberList(String orderId,String userMobile,
                                                  String poiName, String startTime,
                                                  String endTime, int index,
                                                  int pageSize);

    @Select("<script>" +
            "SELECT count(id) FROM poi_member_rd WHERE 1=1 " +
            "<if test=\"orderId != null and orderId.length() != 0 \"> and orderId = #{orderId} </if>" +
            "<if test=\"userMobile != null and userMobile.length()!=0 \"> and userMobile = #{userMobile} </if>" +
            "<if test=\"poiName != null and poiName.length() != 0\"> and poiName LIKE '%${poiName}%' </if>" +
            "<if test=\"startTime != null and startTime.length() != 0\"> and timeEnd &gt;#{startTime}</if>" +
            "<if test=\"endTime != null and endTime.length() != 0\"> and  timeEnd &lt;#{endTime}</if>" +
            "</script>")
    public Integer countPoiMemberRD(String orderId,String userMobile,
                                    String poiName, String startTime,
                                    String endTime);

    @Select("SELECT * FROM poi_member_rd WHERE orderId = #{orderId}")
    public List<PoiMemberRDData> getPoiMemberRDDataByOrderId(String orderId);

    @Select("SELECT * FROM poi_member_rd WHERE orderId = #{orderId}")
    public PoiMemberRDData selectPoiMemberRD(String orderId);

    @Select("<script>" +
            "SELECT poi.id,poi.brandName AS branchName,pm.expiredAt AS expiredAt " +
            "FROM poi,poi_user pu,poi_member pm " +
            "WHERE poi.id=pu.poiId AND poi.id=pm.poiId " +
            "<if test=\"brandName !=null and brandName.length()!=0 \"> AND poi.brandName LIKE '%${brandName}%' </if>" +
            "<if test=\"poiUserMobile !=null and poiUserMobile.length() !=0 \"> AND pu.mobile=#{poiUserMobile} </if>" +
            "<if test=\"startTime !=null and startTime.length() !=0 \"> AND pm.expiredAt &gt; #{startTime} </if>" +
            "<if test=\"endTime !=null and endTime.length() !=0 \"> AND pm.expiredAt &lt; #{endTime} </if>" +
            "ORDER BY pm.expiredAt DESC LIMIT #{index},#{pageSize}"+
            "</script>")
    public List<PoiMemberForm> selectPoiMemberList(@Param("brandName") String brandName,
                                                   @Param("poiUserMobile")String poiUserMobile,
                                                   @Param("startTime") String startTime,
                                                   @Param("endTime") String endTime,
                                                   @Param("index")int index,
                                                   @Param("pageSize")int pageSize);


    @Select("<script>" +
            "SELECT COUNT(*) " +
            "FROM poi,poi_user pu,poi_member pm " +
            "WHERE poi.id=pu.poiId AND poi.id=pm.poiId " +
            "<if test=\"brandName !=null and brandName.length()!=0 \"> AND poi.brandName LIKE '%${brandName}%' </if>" +
            "<if test=\"poiUserMobile !=null and poiUserMobile.length() !=0 \"> AND pu.mobile=#{poiUserMobile} </if>" +
            "<if test=\"startTime !=null and startTime.length() !=0 \"> AND pm.expiredAt &gt; #{startTime} </if>" +
            "<if test=\"endTime !=null and endTime.length() !=0 \"> AND pm.expiredAt &lt; #{endTime} </if>" +
            "</script>")
    public Integer countPoiMember(@Param("brandName") String brandName,
                                  @Param("poiUserMobile")String poiUserMobile,
                                  @Param("startTime") String startTime,
                                  @Param("endTime") String endTime);

    @Select("<script>" +
            "SELECT poi.id,poi.brandName AS branchName,pu.mobile AS poiUserMobile,pm.expiredAt AS expiredAt " +
            "FROM poi,poi_user pu,poi_member pm " +
            "WHERE poi.id=pu.poiId AND poi.id=pm.poiId AND poi.id=#{poiId} " +
            "</script>")
    public PoiMemberForm selectPoiMemberDetail(@Param("poiId") String poiId);

    @Select("<script>" +
            "SELECT pu.name AS name, pu.mobile AS poiUserMobile, pmd.durationUnit AS durationUnit, pmd.timeEnd AS timeEnd, pmd.buyTime AS buyTime " +
            "FROM poi_user pu,poi_member_rd pmd " +
            "WHERE pmd.userId=pu.id AND pmd.poiId = #{poiId}" +
            "</script>")
    public List<PoizMemberList> selectMemberHistory(String poiId);


}
