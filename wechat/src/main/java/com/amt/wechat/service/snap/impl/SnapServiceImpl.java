package com.amt.wechat.service.snap.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amt.wechat.dao.order.OrderDao;
import com.amt.wechat.dao.snap.SnapDao;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.DateTimeUtil;
import com.amt.wechat.form.order.MyOrderItemForm;
import com.amt.wechat.form.order.OrderSubmitForm;
import com.amt.wechat.form.snap.FrameGoods;
import com.amt.wechat.form.snap.SnapCateForm;
import com.amt.wechat.form.snap.SnapGoodsForm;
import com.amt.wechat.model.order.SnapSoldData;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.model.snap.SnapCateData;
import com.amt.wechat.model.snap.SnapGoodsData;
import com.amt.wechat.model.snap.SnapGoodsTemplateData;
import com.amt.wechat.model.snap.SnapTimeframeData;
import com.amt.wechat.service.order.OrderService;
import com.amt.wechat.service.redis.RedisService;
import com.amt.wechat.service.snap.SnapService;
import com.amt.wechat.service.snap.SnapStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


@Service("snapService")
public class SnapServiceImpl implements SnapService {
    private static Logger logger = LoggerFactory.getLogger(SnapServiceImpl.class);

    private @Resource  SnapDao snapDao;
    private @Resource  OrderDao orderDao;

    private @Resource  OrderService orderService;
    private @Resource  RedisService redisService;


    @Override
    public BizPacket snapCateList(){
        List<SnapTimeframeData> timeframeDataList = snapDao.getSnapTimeframeDataList();
        Map<Integer,List<SnapTimeframeData>> timeFramsMap = toMap(timeframeDataList);


        List<SnapCateData> cateDataList =  snapDao.getSnapCateList();
        List<SnapCateForm>  snapCateFormList = toFormList(cateDataList,timeFramsMap);

        return BizPacket.success(snapCateFormList);
    }

    private List<SnapCateForm> toFormList(List<SnapCateData> cateDataList,Map<Integer,List<SnapTimeframeData>> timeFramsMap){

        List<SnapCateForm> formList = new ArrayList<>(cateDataList.size());

        for(SnapCateData e:cateDataList){
            SnapCateForm form = new SnapCateForm();
            formList.add(form);

            form.setCoverImg(e.getCoverImg());
            form.setDrcp(e.getDrcp());
            form.setId(e.getId());
            form.setName(e.getName());
            form.setShowSeq(e.getShowSeq());
            form.setTags(e.getTags());
            form.setStockNum(e.getStockNum());

            List<SnapTimeframeData> timeFrames = timeFramsMap.get(e.getId());
            if(timeFrames == null || timeFrames.isEmpty()){
                continue;
            }

            JSONArray timeFrams = form.getTimeFrames();
            for(SnapTimeframeData timeframeData:timeFrames){

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",timeframeData.getId());
                jsonObject.put("timeStart",timeframeData.getTimeStart());
                jsonObject.put("timeEnd",timeframeData.getTimeEnd());
                jsonObject.put("showSeq",timeframeData.getShowSeq());
                timeFrams.add(jsonObject);
            }
        }
        return formList;
    }



    private Map<Integer,List<SnapTimeframeData>> toMap(List<SnapTimeframeData> allList ){
        Map<Integer,List<SnapTimeframeData>> maps = new HashMap<>();
        for(SnapTimeframeData e:allList){
            List<SnapTimeframeData> list = maps.get(e.getCateId());
            if(list == null){
                list = new ArrayList<>();
                maps.put(e.getCateId(),list);
            }
            list.add(e);
        }
        return maps;
    }

    @Override
    public BizPacket todaySnapGoodsList(int cateId) {

        LocalDate nowDate = LocalDate.now();
        List<SnapGoodsData> snapGoodsDataList = snapDao.getSnapGoodsList(cateId,DateTimeUtil.getDate(nowDate));
        if(snapGoodsDataList == null || snapGoodsDataList.isEmpty()){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"当前无抢购!");
        }


        Map<Integer, SnapGoodsTemplateData> templateDataMap =  snapDao.getSnapTemplateMap(cateId);
        Map<Long, SnapSoldData> soldMap = orderDao.getSnapSoldMap(DateTimeUtil.getDate(LocalDate.now()));

        LocalTime localTime = LocalTime.now();
        Map<String,FrameGoods> frameGoodsMap = new HashMap<>();

        SnapCateData  cateDataData =  snapDao.getSnapCate(cateId);

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
            if(cateDataData != null){
                form.setStockNum(cateDataData.getStockNum());
            }

            frameGoods.getGoodsList().add(form);
        }

        List<FrameGoods> sortableList = new ArrayList<>(frameGoodsMap.values());
        Collections.sort(sortableList,(FrameGoods o1,FrameGoods o2) ->(
                o1.getTimeFrames().compareTo(o2.getTimeFrames())
        ));

        return BizPacket.success(sortableList);
    }


    private Map<Long,SnapGoodsData> getCurrentSnap(int cateId){
        List<SnapGoodsData> snapGoodsDataList = snapDao.getSnapGoodsList(cateId,DateTimeUtil.getDate(LocalDate.now()));
        if(snapGoodsDataList == null || snapGoodsDataList.isEmpty()){
            return Collections.emptyMap();
        }

        LocalTime localTime = LocalTime.now();
        Map<Long,SnapGoodsData> map = new HashMap<>();
        for(SnapGoodsData goodsData:snapGoodsDataList) {
            SnapStatus snapStatus = isTimeIn(localTime, goodsData.getTimeFrameStart(), goodsData.getTimeFrameEnd());
            if (snapStatus != SnapStatus.CURRENT) {
                continue;
            }
            map.put(goodsData.getSeq(),goodsData);
        }
        return map;
    }

    private SnapGoodsForm build(SnapGoodsData goodsData,SnapGoodsTemplateData templateData,SnapSoldData soldData){
        SnapGoodsForm form = new SnapGoodsForm();

        form.setSnapSeq(goodsData.getSeq());
        form.setDisPrice(goodsData.getDisPrice());
        form.setOriPrice(goodsData.getOriPrice());
        form.setGoodsId(goodsData.getGoodsId());

        if(goodsData.getSnapNumEnable() != null){
            form.setSnapNumEnable(goodsData.getSnapNumEnable());
        }else{
            form.setSnapNumEnable(1);
        }


        if(templateData != null) {
            form.setName(templateData.getName());
            form.setCoverImg(templateData.getCoverImg());
            form.setUnitName(templateData.getUnitName());
        }else{
            form.setName("");
            form.setCoverImg("");
            form.setUnitName("项");
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
            //logger.info("start={},now={},end={},current",DateTimeUtil.getTime(ltStart.format(DateTimeUtil.FRIENDLY_TIME_FORMAT)),now.format(DateTimeUtil.FRIENDLY_TIME_FORMAT),ltEnd.format(DateTimeUtil.FRIENDLY_TIME_FORMAT));
            return SnapStatus.CURRENT;
        }
        else if(now.compareTo(ltStart) > 0){
            //logger.info("start={},now={},end={},over",DateTimeUtil.getTime(ltStart.format(DateTimeUtil.FRIENDLY_TIME_FORMAT)),now.format(DateTimeUtil.FRIENDLY_TIME_FORMAT),ltEnd.format(DateTimeUtil.FRIENDLY_TIME_FORMAT));
            return SnapStatus.OVER;
        }else if(now.compareTo(ltEnd) < 0){
            //logger.info("start={},now={},end={},future",DateTimeUtil.getTime(ltStart.format(DateTimeUtil.FRIENDLY_TIME_FORMAT)),now.format(DateTimeUtil.FRIENDLY_TIME_FORMAT),ltEnd.format(DateTimeUtil.FRIENDLY_TIME_FORMAT));
            return SnapStatus.FUTURE;
        }
        return SnapStatus.NONE;
    }


    @Override
    public BizPacket snapOrderSubmit(PoiUserData userData, OrderSubmitForm orderSubmitForm) {
        SnapCateData snapCateData = snapDao.getSnapCate(orderSubmitForm.getCateId());
        if(snapCateData == null){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"此服务不存在:"+ snapCateData.getName());
        }

        Map<Long, SnapGoodsData> currentSnapGoods = getCurrentSnap(orderSubmitForm.getCateId());
        if(currentSnapGoods == null || currentSnapGoods.isEmpty()){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"不存在此类服务:"+orderSubmitForm.getCateId());
        }
        String timeStart = getTimeStart(currentSnapGoods);
        int snapnum = redisService.getSnapNum(userData.getPoiId(),snapCateData.getId(),timeStart);
        if(snapnum >= snapCateData.getStockNum()){
            return BizPacket.error(HttpStatus.GONE.value(),"已售罄:"+snapCateData.getName());
        }


        Map<Integer, SnapGoodsData> goodsDataMap =  snapDao.getSnapGoodsMap(orderSubmitForm.buildSeqs());
        for(MyOrderItemForm myOrderItemForm:orderSubmitForm.getOrderItemList()){
            if(myOrderItemForm.getNum() <= 0 ){
                return BizPacket.error(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value(),"购买数量非法!");
            }

            if(orderSubmitForm.getGoodsType() != 3) {
                return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "参数错误!goodsType="+orderSubmitForm.getGoodsType());
            }

            if(myOrderItemForm.getSnapSeq() <= 0 || myOrderItemForm.getSnapSeq() >= Long.MAX_VALUE){
                return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"抢购序列参数(seq)值越界!");
            }

            SnapGoodsData goodsData = goodsDataMap.get(myOrderItemForm.getSnapSeq());
            if(goodsData == null){
                return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"抢购了不存在的服务:name"+goodsData.getName());
            }

            if(myOrderItemForm.getNum()> goodsData.getSnapNumEnable()){
                return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"每次只能抢购"+goodsData.getSnapNumEnable()+"项:"+goodsData.getName());
            }

            SnapGoodsData currSnapGoods = currentSnapGoods.get(goodsData.getSeq());
            if(currSnapGoods == null){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),goodsData.getName()+":此服务抢购已结束,或者即将开抢!");
            }
        }

        try {
            BizPacket bizPacket =  orderService.orderSubmit(userData,orderSubmitForm);
            if(bizPacket.getCode() == HttpStatus.OK.value()){
                redisService.onSnapSucc(userData.getPoiId(),snapCateData.getId(),timeStart);
            }
            return bizPacket;
        } catch (Exception e) {
            logger.error("userData="+userData+",orderSubmitForm="+orderSubmitForm+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    private String getTimeStart(Map<Long, SnapGoodsData> currentSnapGoods){
        for(SnapGoodsData o:currentSnapGoods.values()){
            return o.getTimeFrameStart();
        }
        return null;
    }
}