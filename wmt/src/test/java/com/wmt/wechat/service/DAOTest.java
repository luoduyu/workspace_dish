package com.wmt.wechat.service;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.commons.util.DateTimeUtil;
import com.wmt.wechat.dao.banner.BannerDao;
import com.wmt.wechat.dao.bidding.BiddingDao;
import com.wmt.wechat.model.banner.BannerData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-19 12:40
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DAOTest {


    private @Resource  BiddingDao biddingDao;
    private @Resource  BannerDao bannerDao;

//    @Test
//    public void testFormSubmit(){
//        BiddingRechargeRd rd = biddingDao.getRechargeData("5");
//        if(rd == null){
//            return;
//        }
//        System.out.println("rd="+rd);

//        rd.setBalance(rd.getBalance()+1);
//        rd.setPayStatus(2);
//        biddingDao.updateRechargeRd(rd,2);
//        System.out.println("OK");
//    }

    @Test
    public void testBanner(){
        String now = DateTimeUtil.getDate(LocalDate.now());
        List<BannerData> list = bannerDao.getBannerDataList(now);
        System.out.println(BizPacket.success(list));
    }
}
