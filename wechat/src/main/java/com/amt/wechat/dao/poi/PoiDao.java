package com.amt.wechat.dao.poi;

import com.amt.wechat.model.poi.PoiData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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

    @Insert("INSERT INTO poi(id, name, accountName, accountPassword, mtAppAuthToken, eleShopId)VALUES(#{id},#{name},#{accountName},#{accountPassword},#{mtAppAuthToken},#{eleShopId})")
    public void addPoiData(PoiData poiData);

}
