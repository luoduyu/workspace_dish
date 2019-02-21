package com.wmt.wechat.controller.wechat.invite;

import com.alibaba.fastjson.JSONObject;
import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.commons.util.DateTimeUtil;
import com.wmt.wechat.controller.wechat.base.BaseController;
import com.wmt.wechat.dao.poi.InviteTop;
import com.wmt.wechat.dao.poi.PoiUserWXCodeDao;
import com.wmt.wechat.model.poi.PoiUserData;
import com.wmt.wechat.model.poi.UserIncomeData;
import com.wmt.wechat.service.poi.PoiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 * 邀请
 *
 * @author adu Create on 2019-02-14 16:42
 * @version 1.0
 */
@RestController
public class InviteController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(InviteController.class);
    private @Resource PoiService poiService;
    private @Resource PoiUserWXCodeDao poiUserWXCodeDao;

    @PostMapping(value = "/invite/getwxacodeunlimit")
    public BizPacket getwxacodeunlimit(Integer pageUrlIndex, String r, String g, String b){
        if(pageUrlIndex == null || pageUrlIndex <0 || pageUrlIndex >= 4){
            return BizPacket.error(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value(),"错误的参数值:"+pageUrlIndex);
        }

        PoiUserData userData  = getUser();
        try {
            return poiService.getWXacodeunlimit(userData,PAGE_URLS[pageUrlIndex],r,g,b);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    private static final String[] PAGE_URLS={
            ""                  ,            // 首页
            "/decoration/material/list",    // 店铺装修首页
            "/go/kaidian/submit",           // 开店申请提交
            "/go/yunying/submit"           // 运营申请提交
    };


    @GetMapping(value = "/invite/home")
    public BizPacket inviteHome(){
        try {
            PoiUserData userData = getUser();
            Integer todayNum = poiUserWXCodeDao.countInviteNum4Date(DateTimeUtil.getDate(LocalDate.now()),userData.getId());
            Integer allNum = poiUserWXCodeDao.countInviteNum4ALL(userData.getId());
            Long totalIncome = poiUserWXCodeDao.sumInviteShare(userData.getId());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("todayNum",todayNum==null?0:todayNum);
            jsonObject.put("allNum",allNum==null?0:allNum);
            jsonObject.put("totalIncome",totalIncome==null?0:totalIncome);
            return BizPacket.success(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @RequestMapping(value = "/invite/today/list",method = {RequestMethod.POST,RequestMethod.GET})
    public BizPacket todayInvite(Integer index,Integer pageSize){
        if(index ==null){
            index =0;
        }
        if(index <0 || index >= Integer.MAX_VALUE){
            index = 0;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        if(pageSize <0 || pageSize >= 200){
            pageSize = 20;
        }

        try {
            PoiUserData userData = getUser();
            List<UserIncomeData> list =  poiUserWXCodeDao.getInviteList4Date(DateTimeUtil.getDate(LocalDate.now()),userData.getId(),index * pageSize,pageSize);
            Integer total = poiUserWXCodeDao.countInviteNum4Date(DateTimeUtil.getDate(LocalDate.now()),userData.getId());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("list",list);
            jsonObject.put("total",total ==null?0:total);

            return BizPacket.success(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }


    @RequestMapping(value = "/invite/all/list",method = {RequestMethod.POST,RequestMethod.GET})
    public BizPacket allInvite(Integer index,Integer pageSize){
        if(index ==null){
            index =0;
        }
        if(index <0 || index >= Integer.MAX_VALUE){
            index = 0;
        }
        if(pageSize == null){
            pageSize = 20;
        }
        if(pageSize <0 || pageSize >= 200){
            pageSize = 20;
        }

        try {
            PoiUserData userData = getUser();
            List<UserIncomeData> list =  poiUserWXCodeDao.getInviteList4ALL(userData.getId(),index * pageSize,pageSize);
            Integer total = poiUserWXCodeDao.countInviteNum4ALL(userData.getId());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("list",list);
            jsonObject.put("total",total==null?0:total);

            return BizPacket.success(jsonObject);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }


    @RequestMapping(value = "/invite/top",method = {RequestMethod.POST,RequestMethod.GET})
    public BizPacket top(){
        try {
            List<InviteTop> list =  poiUserWXCodeDao.getTopInviteIncome(10);
            return BizPacket.success(list);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }
}