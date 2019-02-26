package com.wmt.mgr.service.wechat.bidding;

import com.alibaba.fastjson.JSONObject;
import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.mgr.dao.wechat.bidding.BiddingDao;
import com.wmt.mgr.model.bidding.BiddingPoiForm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service("biddingService")
public class BiddingServiceImpl implements  BiddingService {
    private @Resource BiddingDao biddingDao;

    @Override
    public BizPacket poiList(String brandName, String masterName, String masterMobile, Integer index, Integer pageSize) {
        int s = index * pageSize;
        Integer total = biddingDao.countPoiData(brandName,masterName,masterMobile);
        if(total == null){
            total = 0;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total",total);
        if(total <= 0){
            jsonObject.put("list", Collections.emptyList());
            return BizPacket.success(jsonObject);
        }

        List<BiddingPoiForm> list = biddingDao.findPoiDataList(brandName,masterName,masterMobile,s,pageSize);
        jsonObject.put("list", list);
        return BizPacket.success(jsonObject);
    }
}