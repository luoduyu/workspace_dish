package com.amt.wechat.dao.balance;

import com.amt.wechat.model.balance.BalanceConsumeRd;
import com.amt.wechat.model.balance.BalanceRechargeRD;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
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

    @Insert("INSERT INTO balance_recharge (poiId, userId, userName,amount, rechargeNo, createTime, balance)VALUES(#{poiId},#{userId},#{userName},#{amount},#{rechargeNo},#{createTime},#{balance})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn ="id")
    public void addRechargeRd(BalanceRechargeRD biddingRechargeRd);

    @Select("SELECT * FROM balance_recharge WHERE poiId=#{poiId} ORDER BY createTime DESC LIMIT #{index},#{pageSize}")
    public List<BalanceRechargeRD> getRechargeDataList(String poiId, int index, int pageSize);

    @Select("SELECT COUNT(*) FROM balance_recharge WHERE poiId=#{poiId}")
    public int countRechargeData(String poiId);



    @Select("SELECT * FROM balance_consume WHERE poiId=#{poiId} ORDER BY createTime DESC LIMIT #{index},#{pageSize}")
    public List<BalanceConsumeRd> getConsumeDataList(String poiId, int index, int pageSize);

    @Select("SELECT COUNT(*) FROM balance_consume WHERE poiId=#{poiId}")
    public int countConsumeData(String poiId);

}
