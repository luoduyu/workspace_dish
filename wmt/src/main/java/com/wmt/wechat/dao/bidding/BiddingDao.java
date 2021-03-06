package com.wmt.wechat.dao.bidding;

import com.wmt.wechat.model.bidding.BiddingConsumeRd;
import com.wmt.wechat.model.bidding.BiddingRechargeRd;
import org.apache.ibatis.annotations.*;
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

    @Insert("INSERT INTO bidding_recharge (poiId, userId,userName,amount, orderId, createTime, balance,payStatus,payWay,transactionId,timeEnd,summary)VALUES(#{poiId},#{userId},#{userName},#{amount},#{orderId},#{createTime},#{balance},#{payStatus},#{payWay},#{transactionId},#{timeEnd},#{summary})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn ="id")
    public void addRechargeRd(BiddingRechargeRd biddingRechargeRd);

    @Update("UPDATE bidding_recharge SET payStatus=#{biddingRechargeRd.payStatus},transactionId=#{biddingRechargeRd.transactionId},balance=#{biddingRechargeRd.balance},timeEnd=#{biddingRechargeRd.timeEnd},summary=#{biddingRechargeRd.summary} WHERE id = #{biddingRechargeRd.id} AND payStatus=#{oldPayStatus}")
    public void updateRechargeRd(@Param("biddingRechargeRd") BiddingRechargeRd biddingRechargeRd,@Param("oldPayStatus") int oldPayStatus);

    @Select("SELECT * FROM bidding_recharge WHERE poiId=#{poiId} ORDER BY createTime DESC LIMIT #{index},#{pageSize}")
    public List<BiddingRechargeRd> getRechargeDataList(String poiId, int index, int pageSize);

    @Select("SELECT COUNT(*) FROM bidding_recharge WHERE poiId=#{poiId}")
    public int countRechargeData(String poiId);

    @Select("SELECT * FROM bidding_recharge WHERE id=#{id}")
    public BiddingRechargeRd getRechargeData(String id);




    @Select("SELECT * FROM bidding_consume WHERE poiId=#{poiId} ORDER BY createTime DESC LIMIT #{index},#{pageSize}")
    public List<BiddingConsumeRd> getConsumeDataList(String poiId, int index, int pageSize);

    @Select("SELECT COUNT(*) FROM bidding_consume WHERE poiId=#{poiId}")
    public int countConsumeData(String poiId);
}