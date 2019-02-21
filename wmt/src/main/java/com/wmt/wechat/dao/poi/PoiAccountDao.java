package com.wmt.wechat.dao.poi;

import com.wmt.wechat.model.poi.PoiAccountData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-25 11:23
 * @version 1.0
 */
@Repository("poiAccountDao")
@Mapper
public interface PoiAccountDao {

    @Select("SELECT * FROM poi_account WHERE poiId=#{poiId} LIMIT 1")
    public PoiAccountData getAccountData(String poiId);

    @Insert("INSERT INTO poi_account(poiId,curBalance,curRedBalance,curBiddingBalance)VALUES(#{poiId},#{curBalance},#{curRedBalance},#{curBiddingBalance})")
    public void addPoiAccountData(PoiAccountData data);

    @Update("UPDATE poi_account SET curBiddingBalance = curBiddingBalance + #{amount} WHERE poiId=#{poiId}")
    public void updatePoiBiddingBalance(int amount,String poiId);

    @Update("UPDATE poi_account SET curBalance = #{totalBalance},curRedBalance= #{totalRedBalance}  WHERE poiId=#{poiId}")
    public void updatePoiBalance(int totalBalance,int totalRedBalance,String poiId);
}
