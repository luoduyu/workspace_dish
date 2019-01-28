package com.amt.wechat.dao.snap;

import com.amt.wechat.domain.handler.MyJSONArrayHandler;
import com.amt.wechat.model.snap.SnapCateData;
import com.amt.wechat.model.snap.SnapGoodsData;
import com.amt.wechat.model.snap.SnapGoodsTemplateData;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  抢购
 * @author adu Create on 2019-01-28 11:53
 * @version 1.0
 */
@Repository("snapDao")
@Mapper
public interface SnapDao {


    @Select("SELECT * FROM snap_cate WHERE isEnabled =1 ORDER BY showSeq ASC")
    @Results({
            @Result(property = "timeFrame",column = "timeFrame",typeHandler = MyJSONArrayHandler.class)
    })
    public List<SnapCateData> getSnapCateList();


    @Select("SELECT * FROM snap_template WHERE cateId=#{cateId}")
    @MapKey("goodsId")
    public Map<Integer, SnapGoodsTemplateData> getSnapTemplateMap(int cateId);


    @Select("SELECT * FROM snap_goods WHERE cateId=#{cateId} AND snapDate=#{snapDate} ORDER BY timeFrameStart ASC")
    public List<SnapGoodsData> getSnapGoodsList(int cateId,String snapDate);
}
