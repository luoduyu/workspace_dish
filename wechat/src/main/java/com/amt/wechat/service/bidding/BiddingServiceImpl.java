package com.amt.wechat.service.bidding;

import com.alibaba.fastjson.JSONObject;
import com.amt.wechat.dao.bidding.BiddingDao;
import com.amt.wechat.dao.poi.PoiDao;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.DateTimeUtil;
import com.amt.wechat.model.bidding.BiddingConsumeRd;
import com.amt.wechat.model.bidding.BiddingRechargeRd;
import com.amt.wechat.model.bidding.BiddingStageData;
import com.amt.wechat.model.poi.PoiAccountData;
import com.amt.wechat.model.poi.PoiData;
import com.amt.wechat.model.poi.PoiUserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("biddingService")
public class BiddingServiceImpl implements  BiddingService{
    private static Logger logger = LoggerFactory.getLogger(BiddingServiceImpl.class);

    private @Resource   BiddingDao biddingDao;
    private @Resource  PoiDao poiDao;

    @Override
    public List<BiddingStageData> getStageDataList() {
        return biddingDao.getStageDataList();
    }

    @Override
    public BizPacket getMyRechargeData(PoiUserData userData, int index, int pageSize) {
        try {
            PoiData poiData = poiDao.getPoiData(userData.getPoiId());
            if(poiData == null){
                return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
            }


            int total = biddingDao.countRechargeData(poiData.getId());
            if(total <= 0){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"您还没有充过值!");
            }
            List<BiddingRechargeRd> list = biddingDao.getRechargeDataList(poiData.getId(),index * pageSize,pageSize);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total",total);
            jsonObject.put("list",list);
            return BizPacket.success(jsonObject);
        } catch (Exception e) {
           logger.error("usr="+userData+",e="+e.getMessage(),e);
           return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }


    @Override
    public BizPacket getMyConsumeData(PoiUserData userData, int index, int pageSize){
        try {
            PoiData poiData = poiDao.getPoiData(userData.getPoiId());
            if(poiData == null){
                return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
            }


            int total = biddingDao.countConsumeData(poiData.getId());
            if(total <= 0){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"暂时还没有消费记录!");
            }
            List<BiddingConsumeRd> list = biddingDao.getConsumeDataList(poiData.getId(),index * pageSize,pageSize);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total",total);
            jsonObject.put("list",list);
            return BizPacket.success(jsonObject);
        } catch (Exception e) {
            logger.error("usr="+userData+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }

    @Override
    public BizPacket recharge(PoiUserData userData, int amount) {
        // TODO 请求 一个分布式全局锁

        PoiAccountData accountData =  poiDao.getAccountData(userData.getPoiId());

        try {
            if(accountData == null){
                accountData = createAccount(userData.getPoiId(),amount);
                BiddingRechargeRd rd = createRechargeRd(userData,amount,amount);
                biddingDao.addRechargeRd(rd);

                poiDao.addPoiAccountData(accountData);
                return BizPacket.success(amount);
            }else{
                int currentTotalBiddingBalance = accountData.getCurBiddingBalance() + amount;
                BiddingRechargeRd rd = createRechargeRd(userData,amount,currentTotalBiddingBalance);

                biddingDao.addRechargeRd(rd);
                poiDao.updatePoiBiddingBalance(currentTotalBiddingBalance,userData.getPoiId());
                return BizPacket.success(currentTotalBiddingBalance);
            }

        } catch (Exception e) {
            logger.error("u="+userData+",amount="+amount+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }


    /**
     * 充值rd
     * @param userData
     * @param amount
     * @param currentTotalBiddingBalance
     * @return
     */
    private BiddingRechargeRd createRechargeRd(PoiUserData userData,int amount,int currentTotalBiddingBalance){
        BiddingRechargeRd rd = new BiddingRechargeRd();
        rd.setAmount(amount);
        rd.setAmount(currentTotalBiddingBalance);
        rd.setCreateTime(DateTimeUtil.now());
        rd.setPoiId(userData.getPoiId());
        rd.setUserId(userData.getId());

        // TODO 等待充值完成后回填
        rd.setRechargeNo("");

        return rd;
    }

    private PoiAccountData createAccount(String poiId,int amount){
        PoiAccountData data = new PoiAccountData();
        data.setPoiId(poiId);
        data.setCurBiddingBalance(amount);
        data.setCurBalance(0);
        data.setCurRedBalance(0);
        data.setCurrShareBalance(0);
        return data;
    }
}