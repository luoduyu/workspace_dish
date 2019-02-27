package com.wmt.wechat.controller.wechat.banner;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.commons.util.DateTimeUtil;
import com.wmt.wechat.dao.banner.BannerDao;
import com.wmt.wechat.model.banner.BannerData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-27 16:51
 * @version 1.0
 */
@RestController
public class BannerController {
    private @Resource BannerDao bannerDao;

    @GetMapping(value = "/banner/list")
    public BizPacket bannerList(){
        String now = DateTimeUtil.getDate(LocalDate.now());
        List<BannerData> list = bannerDao.getBannerDataList(now);

        if(list == null || list.isEmpty()){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"未设置");
        }
        return BizPacket.success(list);
    }
}
