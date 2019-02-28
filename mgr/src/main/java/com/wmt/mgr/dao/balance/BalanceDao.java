package com.wmt.mgr.dao.balance;

import com.wmt.mgr.model.balance.BalanceConsumeData;
import com.wmt.mgr.model.balance.BalanceRechargeData;
import com.wmt.mgr.model.balance.BalanceSettingData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

    @Select("SELECT * FROM balance_setting WHERE isEnabled=1 LIMIT 1")
    public BalanceSettingData getGlobalSettingData();

    @Select("SELECT * FROM balance_consume WHERE orderId = #{orderId}")
    public BalanceConsumeData selectBalanceConsumeByOrderId(@Param("orderId") String orderId);

    @Select("SELECT * FROM balance_recharge WHERE orderId = #{orderId}")
    public BalanceRechargeData selectBalanceRechargeByOrderId(@Param("orderId") String orderId);

    @Select("SELECT * FROM balance_recharge WHERE poiId = #{poiId}")
    public List<BalanceRechargeData> selectBalanceRechargeByPoiId(String poiId);
}
