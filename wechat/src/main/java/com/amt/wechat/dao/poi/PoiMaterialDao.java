package com.amt.wechat.dao.poi;

import com.amt.wechat.model.poi.MaterialData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-03 20:17
 * @version 1.0
 */
@Repository("poiMaterialDao")
@Mapper
public interface PoiMaterialDao {
    @Select("SELECT * FROM poi_material ORDER BY showSeq ASC")
    public List<MaterialData> getPoiMaterialDataList();
}
