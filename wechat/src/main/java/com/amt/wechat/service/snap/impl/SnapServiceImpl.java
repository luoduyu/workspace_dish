package com.amt.wechat.service.snap.impl;

import com.amt.wechat.dao.order.OrderDao;
import com.amt.wechat.dao.snap.SnapDao;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.DateTimeUtil;
import com.amt.wechat.form.snap.FrameGoods;
import com.amt.wechat.form.snap.SnapGoodsForm;
import com.amt.wechat.model.snap.SnapGoodsData;
import com.amt.wechat.model.snap.SnapGoodsTemplateData;
import com.amt.wechat.model.order.SnapSoldData;
import com.amt.wechat.service.snap.SnapService;
import com.amt.wechat.service.snap.SnapStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("snapService")
public class SnapServiceImpl implements SnapService {
    private @Resource  SnapDao snapDao;
    private @Resource  OrderDao orderDao;

    @Override
    public BizPacket todaySnapGoodsList(int cateId) {

        LocalDate nowDate = LocalDate.now();
        List<SnapGoodsData> snapGoodsDataList = snapDao.getSnapGoodsList(cateId,DateTimeUtil.getDate(nowDate));
        if(snapGoodsDataList == null || snapGoodsDataList.isEmpty()){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"当前无抢购!");
        }

        LocalTime localTime = LocalTime.now();

        Map<Integer, SnapGoodsTemplateData> templateDataMap =  snapDao.getSnapTemplateMap(cateId);
        Map<Long, SnapSoldData> soldMap = orderDao.getSnapSoldMap(DateTimeUtil.getDate(LocalDate.now()));


        Map<String,FrameGoods> frameGoodsMap = new HashMap<>();
        for(SnapGoodsData goodsData:snapGoodsDataList){
            SnapStatus snapStatus = isTimeIn(localTime,goodsData.getTimeFrameStart(),goodsData.getTimeFrameEnd());
            if(snapStatus == SnapStatus.NONE){
                continue;
            }

            FrameGoods frameGoods =  frameGoodsMap.get(goodsData.getTimeFrameStart());
            if(frameGoods == null){
                frameGoods = new FrameGoods(snapStatus.ordinal(),goodsData.getTimeFrameStart());
                frameGoodsMap.put(goodsData.getTimeFrameStart(),frameGoods);
            }

            SnapGoodsForm form = build(goodsData,templateDataMap.get(goodsData.getGoodsId()),soldMap.get(goodsData.getSeq()));
            frameGoods.getGoodsList().add(form);
        }

        return BizPacket.success(frameGoodsMap.values());
    }



    private SnapGoodsForm build(SnapGoodsData goodsData,SnapGoodsTemplateData templateData,SnapSoldData soldData){
        SnapGoodsForm form = new SnapGoodsForm();

        form.setSnapSeq(goodsData.getSeq());
        form.setDisPrice(goodsData.getDisPrice());
        form.setOriPrice(goodsData.getOriPrice());
        form.setGoodsId(goodsData.getGoodsId());
        form.setStockNum(goodsData.getStockNum());

        if(templateData != null) {
            form.setName(templateData.getName());
            form.setCoverImg(templateData.getCoverImg());
            form.setUnitName(templateData.getUnitName());
            form.setSnapNumEnable(templateData.getSnapNumEnable());
        }else{
            form.setName("");
            form.setCoverImg("");
            form.setUnitName("项");
            form.setSnapNumEnable(1);
        }
        if(soldData != null){
            form.setSoldNum(soldData.getSoldNum());
        }else{
            form.setSoldNum(0);
        }

        return form;
    }

    private SnapStatus isTimeIn(LocalTime now, String startTime,String endTime){
        LocalTime ltStart =  DateTimeUtil.getTime(startTime);
        LocalTime ltEnd =  DateTimeUtil.getTime(endTime);

        if(now.compareTo(ltStart) >= 0 && now.compareTo(ltEnd) <= 0){
            return SnapStatus.CURRENT;
        }
        else if(now.compareTo(ltStart) < 0){
            return SnapStatus.OVER;
        }else if(now.compareTo(ltEnd) >0){
            return SnapStatus.FUTURE;
        }
        return SnapStatus.NONE;
    }
}