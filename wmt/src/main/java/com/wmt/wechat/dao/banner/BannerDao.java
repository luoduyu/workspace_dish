package com.wmt.wechat.dao.banner;

import com.wmt.wechat.model.banner.BannerData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-27 16:41
 * @version 1.0
 */
@Repository("bannerDao")
@Mapper
public interface BannerDao {


    @Select("SELECT * FROM home_banners WHERE isEnabled=1 AND #{nowDate} >= expiredStart AND #{nowDate} <= expiredEnd ORDER BY showSeq DESC LIMIT 8")
    public List<BannerData> getBannerDataList(String nowDate);

}
