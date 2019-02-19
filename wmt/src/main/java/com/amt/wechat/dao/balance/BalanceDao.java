package com.amt.wechat.dao.balance;

import com.amt.wechat.model.balance.BalanceConsumeRd;
import com.amt.wechat.model.balance.BalanceRechargeRD;
import com.amt.wechat.model.balance.BalanceSettingData;
import com.amt.wechat.model.balance.CurrencyStageData;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-12 11:21
 * @version 1.0
 */
@Repository("balanceDao")
@Mapper
public interface BalanceDao {

    @Insert("INSERT INTO balance_recharge (poiId, userId,userName,amount, orderId, createTime, balance,redBalance,payStatus,payWay,transactionId,timeEnd,summary)VALUES(#{poiId},#{userId},#{userName},#{amount},#{orderId},#{createTime},#{balance},#{redBalance},#{payStatus},#{payWay},#{transactionId},#{timeEnd},#{summary})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn ="id")
    public void addRechargeRd(BalanceRechargeRD balanceRechargeRD);


    @Update("UPDATE balance_recharge SET payStatus=#{balanceRechargeRD.payStatus},transactionId=#{balanceRechargeRD.transactionId},balance=#{balanceRechargeRD.balance},redBalance=#{balanceRechargeRD.redBalance},timeEnd=#{balanceRechargeRD.timeEnd},summary=#{balanceRechargeRD.summary} WHERE id = #{balanceRechargeRD.id} AND payStatus=#{oldPayStatus}")
    public void updateRechargeRd(@Param("balanceRechargeRD") BalanceRechargeRD balanceRechargeRD,@Param("oldPayStatus") int oldPayStatus);

    @Select("SELECT * FROM balance_recharge WHERE poiId=#{poiId} ORDER BY createTime DESC LIMIT #{index},#{pageSize}")
    public List<BalanceRechargeRD> getRechargeDataList(String poiId, int index, int pageSize);

    @Select("SELECT COUNT(*) FROM balance_recharge WHERE poiId=#{poiId}")
    public int countRechargeData(String poiId);

    @Select("SELECT * FROM balance_recharge WHERE id=#{id}")
    public BalanceRechargeRD getRechargeData(String id);


    @Select("SELECT * FROM currency_stage ORDER BY showSeq ASC LIMIT 100")
    public List<CurrencyStageData> getStageDataList();




    @Insert("INSERT INTO balance_consume (poiId,orderId,createTime,summary,userId,userName,cateId,amount)VALUES(#{poiId},#{orderId},#{createTime},#{summary},#{userId},#{userName},#{cateId},#{amount})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn ="id")
    public void addConsumeRd(BalanceConsumeRd consumeRd);

    @Select("SELECT * FROM balance_consume WHERE poiId=#{poiId} ORDER BY createTime DESC LIMIT #{index},#{pageSize}")
    public List<BalanceConsumeRd> getConsumeDataList(String poiId, int index, int pageSize);

    @Select("SELECT COUNT(*) FROM balance_consume WHERE poiId=#{poiId}")
    public int countConsumeData(String poiId);



    @Select("SELECT * FROM balance_setting WHERE isEnabled=1 LIMIT 1")
    public BalanceSettingData getGlobalSettingData();
}
