package com.amt.wechat.dao.member;

import com.amt.wechat.model.poi.PoiMemberData;
import com.amt.wechat.model.poi.PoiMemberRDData;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  店铺会员(店铺的‘会员’指的就是店铺，而不是操控店铺的'人')
 * @author adu Create on 2019-01-25 11:24
 * @version 1.0
 */
@Repository("poiMemberDao")
@Mapper
public interface PoiMemberDao {

    @Insert("INSERT INTO poi_member(poiId,durationUnit,duration,buyTime,expiredAt,autoFeeRenew,autoFee) VALUES(#{poiId},#{durationUnit},#{duration},#{buyTime},#{expiredAt},#{autoFeeRenew},#{autoFee})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public void addPoiMemberData(PoiMemberData memberData);

    @Update("UPDATE poi_member SET durationUnit=#{durationUnit},duration=#{duration},buyTime=#{buyTime},expiredAt=#{expiredAt},autoFeeRenew=#{autoFeeRenew},autoFee=#{autoFee} WHERE poiId = #{poiId}")
    public void updatePoiMemberData(PoiMemberData memberData);

    @Update("UPDATE poi_member SET autoFeeRenew=#{autoFeeRenew} WHERE poiId = #{poiId}")
    public void updatePoiMemberFreeRenew(int autoFeeRenew,String poiId);

    @Select("SELECT * FROM poi_member WHERE poiId=#{poiId} LIMIT 1")
    public PoiMemberData getPoiMemberData(String poiId);

    @Update("UPDATE poi_member SET autoFeeRenew=#{autoFeeRenew} WHERE poiId=#{poiId}")
    public void updateMemberFeeAutoRenew(int autoFeeRenew,String poiId);



    @Insert("INSERT INTO poi_member_rd(poiId,durationUnit,duration,buyTime,userId,total,discount,payment,payStatus,payWay,orderId,transactionId,timeEnd,summary,feeRenew) VALUES(#{poiId},#{durationUnit},#{duration},#{buyTime},#{userId},#{total},#{discount},#{payment},#{payStatus},#{payWay},#{orderId},#{transactionId},#{timeEnd},#{summary},#{feeRenew})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public void addMemberBoughtRD(PoiMemberRDData rdData);


    @Update("UPDATE poi_member_rd SET discount=#{discount},payment=#{payment},payWay=#{payWay},payStatus=#{payStatus},transactionId=#{transactionId},timeEnd=#{timeEnd},summary=#{summary} WHERE id = #{id}")
    public void updateMemberBoughtRD(PoiMemberRDData rdData);

    @Select("SELECT * FROM poi_member_rd WHERE orderId=#{orderId} LIMIT 1")
    public PoiMemberRDData getMemberBoughtRDByOrderId(String orderId);

    @Select("SELECT * FROM poi_member_rd WHERE id=#{id} LIMIT 1")
    public PoiMemberRDData getMemberBoughtRDById(String id);


    /**
     *  购买会员的次数
     * @param poiId
     * @return
     */
    @Select("SELECT COUNT(id) FROM poi_member_rd WHERE poiId=#{poiId} AND payStatus=2")
    public int countPoiMemberRD(String poiId);
    /**
     * 会员卡购买记录
     * @param poiId
     * @param index
     * @param pageSize
     * @return
     */
    @Select("SELECT * FROM poi_member_rd WHERE poiId=#{poiId} ORDER BY buyTime DESC LIMIT #{index},#{pageSize}")
    public List<PoiMemberRDData> getMemberBoughtList(String poiId, int index, int pageSize);
}
