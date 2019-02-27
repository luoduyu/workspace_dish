package com.wmt.mgr.controller.wechat.banner;

import com.alibaba.fastjson.JSONObject;
import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.dao.banner.BannerDao;
import com.wmt.mgr.domain.annotation.PostMappingEx;
import com.wmt.mgr.domain.rabc.permission.MgrModules;
import com.wmt.mgr.model.banner.BannerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * Copyright (c) 2019 by CANSHU
 *  首页BANNER
 * @author adu Create on 2019-02-27 17:17
 * @version 1.0
 */
@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
public class BannerController {
    private static Logger logger = LoggerFactory.getLogger(BannerController.class);

    private @Resource BannerDao bannerDao;

    @PostMappingEx(value = "/mgr/banner/list",funcName = "首页banner浏览",module = MgrModules.BASIC)
    public BizPacket bannerList(String name, @RequestParam("isEnabled") Integer isEnabled, String expiredStart, String expiredEnd, String createDateStart, String createDateEnd, Integer index, Integer pageSize){
        if(isEnabled == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"isEnabled参数错误!-1/0/1");
        }
        if(isEnabled != -1 && isEnabled != 0 && isEnabled != 1){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"isEnabled参数错误!-1/0/1");
        }
        if(index == null || index <0 || index > Integer.MAX_VALUE)
        {
            index = 0;
        }

        if(pageSize == null || pageSize <0 || pageSize > 600)
        {
            index = 20;
        }
        if(name!= null){
            name = name.trim();
        }
        if(expiredStart!= null){
            expiredStart = expiredStart.trim();
        }
        if(expiredEnd!= null){
            expiredEnd = expiredEnd.trim();
        }
        if(createDateStart!= null){
            createDateStart = createDateStart.trim();
        }
        if(createDateEnd!= null){
            createDateEnd = createDateEnd.trim();
        }

        try {
            Integer total = bannerDao.countBannerData(name,isEnabled,expiredStart,expiredEnd,createDateStart,createDateEnd);
            if(total == null){
                total = 0;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total",total);
            if(total <=0){
                jsonObject.put("list", Collections.emptyList());
                return BizPacket.success(jsonObject);
            }

            List<BannerData> list  = bannerDao.getBannerDataList(name,isEnabled,expiredStart,expiredEnd,createDateStart,createDateEnd,index*pageSize,pageSize);
            jsonObject.put("list",list);
            return BizPacket.success(jsonObject);
        } catch (Exception e) {
           logger.error("name="+name+",isEnabled="+isEnabled+",expiredStart="+expiredStart+",expiredEnd="+expiredEnd+",createDateStart="+createDateStart+",createDateEnd="+createDateEnd+",e="+e.getMessage(),e);
           return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @PostMappingEx(value = "/mgr/banner/rm",funcName = "首页banner删除",module = MgrModules.BASIC)
    public BizPacket bannerRM(Integer id){
        if(id == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"id参数必传");
        }

        try {
            bannerDao.bannerRM(id);
            return BizPacket.success();
        } catch (Exception e) {
            logger.error("id="+id+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @PostMappingEx(value = "/mgr/banner/detail",funcName = "首页banner详情",module = MgrModules.BASIC)
    public BizPacket bannerDetail(Integer id){
        if(id == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"id参数必传");
        }

        try {
            Integer showSeqMax = bannerDao.getBannerMaxShowSeq();
            if(showSeqMax == null){
                showSeqMax = 1;
            }
            JSONObject jsonObject = new JSONObject();

            // 建议顺序号
            jsonObject.put("suggestSeq",showSeqMax);

            BannerData data = bannerDao.getBannerData(id);
            jsonObject.put("data",data);

            return BizPacket.success(jsonObject);
        } catch (Exception e) {
            logger.error("id="+id+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @PostMappingEx(value = "/mgr/banner/edit",funcName = "首页banner编辑",module = MgrModules.BASIC)
    public BizPacket bannerEdit(@RequestBody @Valid BannerData bannerData){
        if(bannerData == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"id参数必传");
        }
        if(bannerData.getId() == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"banner.id参数必传");
        }

        try {
            bannerDao.updateBannerData(bannerData);
            return BizPacket.success();
        } catch (Exception e) {
            logger.error("bannerData="+bannerData+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @PostMappingEx(value = "/mgr/banner/add",funcName = "首页banner编辑",module = MgrModules.BASIC)
    public BizPacket bannerAdd(@RequestBody @Valid BannerData bannerData){
        if(bannerData == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"id参数必传");
        }

        try {
            bannerDao.addBannerData(bannerData);
            return BizPacket.success(bannerData.getId());
        } catch (Exception e) {
            logger.error("bannerData="+bannerData+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }
}