package com.wmt.wechat.service.snap.impl;

import com.wmt.commons.domain.packet.BizPacket;
import com.wmt.commons.util.DateTimeUtil;
import com.wmt.wechat.dao.order.OrderDao;
import com.wmt.wechat.dao.snap.SnapDao;
import com.wmt.wechat.form.order.MyOrderItemForm;
import com.wmt.wechat.form.order.OrderSubmitForm;
import com.wmt.wechat.form.snap.FrameGoods;
import com.wmt.wechat.form.snap.SnapCateForm;
import com.wmt.wechat.form.snap.SnapGoodsForm;
import com.wmt.wechat.model.poi.PoiUserData;
import com.wmt.wechat.model.snap.SnapCateData;
import com.wmt.wechat.model.snap.SnapGoodsData;
import com.wmt.wechat.service.order.OrderService;
import com.wmt.wechat.service.redis.RedisService;
import com.wmt.wechat.service.snap.SnapService;
import com.wmt.wechat.service.snap.SnapStatus;
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

    private @Resource
    SnapDao snapDao;
    private @Resource
    OrderDao orderDao;

    private @Resource  OrderService orderService;
    private @Resource
    RedisService redisService;


    @Override
    public BizPacket snapCateList(){
        List<SnapCateData> cateDataList =  snapDao.getSnapCateList();
        List<SnapCateForm>  snapCateFormList = toFormList(cateDataList);
        return BizPacket.success(snapCateFormList);
    }

    private List<SnapCateForm> toFormList(List<SnapCateData> cateDataList){

        List<SnapCateForm> formList = new ArrayList<>(cateDataList.size());

        for(SnapCateData e:cateDataList){
            SnapCateForm form = new SnapCateForm();
            formList.add(form);

            form.setId(e.getId());
            form.setName(e.getName());
            form.setCoverImg(e.getCoverImg());
            form.setDrcp(e.getDrcp());
            form.setTags(e.getTags());
            form.setShowSeq(e.getShowSeq());
        }
        return formList;
    }

    @Override
    public BizPacket todaySnapGoodsList(int cateId) {

        LocalDate nowDate = LocalDate.now();
        List<SnapGoodsData> snapGoodsDataList = snapDao.getSnapGoodsList(cateId,DateTimeUtil.getDate(nowDate));
        if(snapGoodsDataList == null || snapGoodsDataList.isEmpty()){
            return BizPacket.error(HttpStatus.NOT_FOUND.value(),"当前无抢购!");
        }

        Map<String, FrameGoods> frameGoodsMap = new HashMap<>();
        Map<String,Integer> soldNumMap = redisService.countTodaySnapSoldNum(cateId);


        LocalTime localTime = LocalTime.now();
        for(SnapGoodsData goodsData:snapGoodsDataList){
            SnapStatus snapStatus = isTimeIn(localTime,goodsData.getTimeStart(),goodsData.getTimeEnd());
            if(snapStatus == SnapStatus.NONE){
                continue;
            }

            FrameGoods frameGoods =  frameGoodsMap.get(goodsData.getTimeStart());
            if(frameGoods == null){
                String hhmmss = goodsData.getTimeStart().replaceAll(":","");
                frameGoods = new FrameGoods(snapStatus,goodsData.getTimeStart(),goodsData.getTimeEnd(),goodsData.getStockNum(),soldNumMap.get(hhmmss));
                frameGoodsMap.put(goodsData.getTimeStart(),frameGoods);
            }

            SnapGoodsForm form = build(goodsData);
            frameGoods.getGoodsList().add(form);
        }

        List<FrameGoods> sortableList = new ArrayList<>(frameGoodsMap.values());
        Collections.sort(sortableList,(FrameGoods o1,FrameGoods o2) ->(
                o1.getTimeFrameStart().compareTo(o2.getTimeFrameStart())
        ));

        return BizPacket.success(sortableList);
    }


    /**
     * 当天-当前类别-当前时段正在售卖的物品或服务
     * @param cateId
     * @return
     */
    private Map<Long,SnapGoodsData> getCurrentSnap(int cateId){
        List<SnapGoodsData> snapGoodsDataList = snapDao.getSnapGoodsList(cateId, DateTimeUtil.getDate(LocalDate.now()));
        if(snapGoodsDataList == null || snapGoodsDataList.isEmpty()){
            return Collections.emptyMap();
        }

        LocalTime localTime = LocalTime.now();
        Map<Long,SnapGoodsData> map = new HashMap<>();
        for(SnapGoodsData goodsData:snapGoodsDataList) {
            SnapStatus snapStatus = isTimeIn(localTime, goodsData.getTimeStart(), goodsData.getTimeEnd());
            if (snapStatus != SnapStatus.CURRENT) {
                continue;
            }
            map.put(goodsData.getSeq(),goodsData);
        }
        return map;
    }

    private SnapGoodsForm build(SnapGoodsData goodsData){
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

        form.setName(goodsData.getName());
        form.setCoverImg(goodsData.getCoverImg());
        form.setUnitName(goodsData.getUnitName());

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

        // 拿到当前类别下应该正在抢购的物品
        Map<Long, SnapGoodsData> currentSnapGoods = getCurrentSnap(orderSubmitForm.getCateId());
        if(currentSnapGoods == null || currentSnapGoods.isEmpty()){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"此类服务已售:"+orderSubmitForm.getCateId());
        }
        if(!isCateExist(currentSnapGoods.values(),orderSubmitForm.getCateId())){
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"此服务不存在:"+ orderSubmitForm.getCateId());
        }

        // 任意取其中一个
        SnapGoodsData anyCurrentGoodsData = getCurrentGoodsData(currentSnapGoods);
        int snapnum = redisService.getSnapNum(anyCurrentGoodsData.getCateId(),anyCurrentGoodsData.getTimeStart());
        if(snapnum >= anyCurrentGoodsData.getStockNum()){
            return BizPacket.error(HttpStatus.GONE.value(),"本时段已全部售罄:"+anyCurrentGoodsData.getName());
        }


        String seqs = orderSubmitForm.buildSeqs();
        logger.info("{}提交抢购订单,snapSeqs={},orderSubmitForm={}",userData,seqs,orderSubmitForm);


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

            SnapGoodsData goodsData = currentSnapGoods.get(myOrderItemForm.getSnapSeq());
            if(goodsData == null){
                return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"抢购了不存在的服务:"+goodsData.getName());
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
            BizPacket bizPacket =  orderService.orderSnapSubmit(userData,orderSubmitForm,currentSnapGoods);
            if(bizPacket.getCode() == HttpStatus.OK.value()){
                redisService.onSnapSucc(anyCurrentGoodsData.getCateId(),anyCurrentGoodsData.getTimeStart());
            }
            return bizPacket;
        } catch (Exception e) {
            logger.error("userData="+userData+",orderSubmitForm="+orderSubmitForm+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }


    private boolean isCateExist(Collection<SnapGoodsData> currentSnapGoods,int cateId){
        for(SnapGoodsData e:currentSnapGoods){
            if(e.getCateId() == cateId){
                return true;
            }
        }
        return false;
    }
    private SnapGoodsData getCurrentGoodsData(Map<Long, SnapGoodsData> currentSnapGoods){
        for(SnapGoodsData o:currentSnapGoods.values()){
            return o;
        }
        return null;
    }
}