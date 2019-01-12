package com.amt.wechat.dao.bidding;

import com.amt.wechat.model.bidding.BiddingConsumeRd;
import com.amt.wechat.model.bidding.BiddingRechargeRd;
import com.amt.wechat.model.bidding.BiddingStageData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-08 17:15
 * @version 1.0
 */
@Repository("biddingDao")
@Mapper
public interface BiddingDao {

    @Select("SELECT * FROM bidding_stage ORDER BY showSeq ASC LIMIT 100")
    public List<BiddingStageData> getStageDataList();



    @Insert("INSERT INTO bidding_recharge (poiId, userId,userName,amount, rechargeNo, createTime, balance)VALUES(#{poiId},#{userId},#{userName},#{amount},#{rechargeNo},#{createTime},#{balance})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn ="id")
    public void addRechargeRd(BiddingRechargeRd biddingRechargeRd);

    @Select("SELECT * FROM bidding_recharge WHERE poiId=#{poiId} ORDER BY createTime DESC LIMIT #{index},#{pageSize}")
    public List<BiddingRechargeRd> getRechargeDataList(String poiId, int index, int pageSize);

    @Select("SELECT COUNT(*) FROM bidding_recharge WHERE poiId=#{poiId}")
    public int countRechargeData(String poiId);



    @Select("SELECT * FROM bidding_consume WHERE poiId=#{poiId} ORDER BY createTime DESC LIMIT #{index},#{pageSize}")
    public List<BiddingConsumeRd> getConsumeDataList(String poiId, int index, int pageSize);

    @Select("SELECT COUNT(*) FROM bidding_consume WHERE poiId=#{poiId}")
    public int countConsumeData(String poiId);



}
