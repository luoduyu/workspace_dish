package com.amt.wechat.service;

import com.amt.wechat.dao.bidding.BiddingDao;
import com.amt.wechat.model.bidding.BiddingRechargeRd;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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

    @Test
    public void testFormSubmit(){
        BiddingRechargeRd rd = biddingDao.getRechargeData("5");
        if(rd == null){
            return;
        }
        System.out.println("rd="+rd);

//        rd.setBalance(rd.getBalance()+1);
//        rd.setPayStatus(2);
//        biddingDao.updateRechargeRd(rd,2);
//        System.out.println("OK");
    }
}
