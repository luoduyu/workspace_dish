package com.wmt.wechat.dao.snap;

import com.wmt.wechat.model.snap.SnapCateData;
import com.wmt.wechat.model.snap.SnapGoodsData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public List<SnapCateData> getSnapCateList();

    @Select("SELECT sd.*,sdt.timeStart,sdt.timeEnd,sdt.stockNum FROM snap_goods sd,snap_goods_timeframe sdt WHERE cateId=#{cateId} AND snapDate=#{snapDate} AND sd.timeFrameId=sdt.id")
    public List<SnapGoodsData> getSnapGoodsList(int cateId, String snapDate);
}
