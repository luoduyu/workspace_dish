package com.amt.wechat.dao.poi;

import com.amt.wechat.model.poi.PoiAccountData;
import com.amt.wechat.model.poi.PoiData;
import com.amt.wechat.model.poi.PoiMemberData;
import com.amt.wechat.model.poi.PoiMemberRDData;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  门店数据
 *
 * @author adu Create on 2019-01-03 18:28
 * @version 1.0
 */
@Repository("poiDao")
@Mapper
public interface PoiDao {

    @Insert("INSERT INTO poi(id,name,contry,province,city,districts,street,address,brandName,cateId,accountName,accountPassword, mtAppAuthToken,eleShopId,createTime,updTime)VALUES(#{id},#{name},#{contry},#{province},#{city},#{districts},#{street},#{address},#{brandName},#{cateId},#{accountName},#{accountPassword},#{mtAppAuthToken},#{eleShopId},#{createTime}, #{updTime})")
    public void addPoiData(PoiData poiData);

    @Select("SELECT * FROM poi WHERE id=#{id} LIMIT 1")
    public PoiData getPoiData(String id);

    @Update("UPDATE poi SET contry=#{contry},province=#{province},city =#{city},districts =#{districts},street =#{street},address =#{address},brandName =#{brandName},cateId=#{cateId},updTime=#{updTime} WHERE id = #{id}")
    public void updatePoiData(PoiData poiData);

    @Update("UPDATE poi SET balancePwd=#{pwd} WHERE id=#{poiId}")
    public void updateBalancePwd(String pwd,String poiId);




    @Select("SELECT * FROM poi_account WHERE poiId=#{poiId} LIMIT 1")
    public PoiAccountData getAccountData(String poiId);

    @Insert("INSERT INTO poi_account(poiId,curBalance,curRedBalance,curBiddingBalance,currShareBalance)VALUES(#{poiId},#{curBalance},#{curRedBalance},#{curBiddingBalance},#{currShareBalance})")
    public void addPoiAccountData(PoiAccountData data);

    @Update("UPDATE poi_account SET curBiddingBalance = curBiddingBalance+ #{amount} WHERE poiId=#{poiId}")
    public void updatePoiBiddingBalance(int amount,String poiId);

    @Update("UPDATE poi_account SET curBalance = curBalance + #{amount} WHERE poiId=#{poiId}")
    public void updatePoiBalance(int amount,String poiId);




    @Insert("INSERT INTO poi_member(poiId,durationUnit,duration,buyTime,expiredAt,autoFeeRenew,autoFee) VALUES(#{poiId},#{durationUnit},#{duration},#{buyTime},#{expiredAt},#{autoFeeRenew},#{autoFee})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public void addPoiMemberData(PoiMemberData memberData);

    @Update("UPDATE poi_member SET durationUnit=#{durationUnit},duration=#{duration},buyTime=#{buyTime},expiredAt=#{expiredAt},autoFeeRenew=#{autoFeeRenew},autoFee=#{autoFee} where poiId = #{poiId}")
    public void updatePoiMemberData(PoiMemberData memberData);

    @Select("SELECT * FROM poi_member WHERE poiId=#{poiId} LIMIT 1")
    public PoiMemberData getPoiMemberData(String poiId);

    @Update("UPDATE poi_member SET feeRenew=#{feeRenew} WHERE poiId=#{poiId}")
    public void updateMemberFeeAutoRenew(int feeRenew,String poiId);






    @Insert("INSERT INTO poi_member_rd(poiId,durationUnit,duration,buyTime,userId,total,newDiscount,payment,payStatus,payTime,payNo,feeRenew)VALUES(#{poiId},#{durationUnit},#{duration},#{buyTime},#{userId},#{total},#{newDiscount},#{payment},#{payStatus},#{payTime},#{payNo},#{feeRenew})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    public void addMemberBoughtRD(PoiMemberRDData rdData);
    /**
     *  购买会员的次数
     * @param poiId
     * @return
     */
    @Select("SELECT COUNT(id) FROM poi_member_rd WHERE poiId=#{poiId}")
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