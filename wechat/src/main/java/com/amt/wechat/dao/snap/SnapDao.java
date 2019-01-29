package com.amt.wechat.dao.snap;

import com.amt.wechat.model.snap.SnapCateData;
import com.amt.wechat.model.snap.SnapGoodsData;
import com.amt.wechat.model.snap.SnapGoodsTemplateData;
import com.amt.wechat.model.snap.SnapTimeframeData;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
    public List<SnapCateData> getSnapCateList();

    @Select("SELECT * FROM snap_cate WHERE isEnabled =1 AND id=#{cateId} ORDER BY showSeq ASC")
    public SnapCateData getSnapCate(int cateId);


    @Select("SELECT * FROM snap_timeframes ORDER BY timeStart ASC")
    public List<SnapTimeframeData> getSnapTimeframeDataList();


    @Select("SELECT * FROM snap_template WHERE cateId=#{cateId}")
    @MapKey("goodsId")
    public Map<Integer, SnapGoodsTemplateData> getSnapTemplateMap(int cateId);


    @Select("SELECT * FROM snap_goods WHERE cateId=#{cateId} AND snapDate=#{snapDate} ORDER BY timeFrameStart ASC")
    public List<SnapGoodsData> getSnapGoodsList(int cateId,String snapDate);



    @Select("SELECT * FROM snap_goods WHERE seq IN(${seqs})")
    @MapKey("seq")
    public Map<Integer, SnapGoodsData> getSnapGoodsMap(@Param("seqs") String seqs);
}
