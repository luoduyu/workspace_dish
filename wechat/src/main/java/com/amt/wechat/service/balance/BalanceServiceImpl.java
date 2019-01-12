package com.amt.wechat.service.balance;

import com.alibaba.fastjson.JSONObject;
import com.amt.wechat.dao.balance.BalanceDao;
import com.amt.wechat.dao.poi.PoiDao;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.DateTimeUtil;
import com.amt.wechat.model.balance.BalanceConsumeRd;
import com.amt.wechat.model.balance.BalanceRechargeRD;
import com.amt.wechat.model.poi.PoiAccountData;
import com.amt.wechat.model.poi.PoiData;
import com.amt.wechat.model.poi.PoiUserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("balanceService")
public class BalanceServiceImpl implements  BalanceService {

    private static Logger logger = LoggerFactory.getLogger(BalanceServiceImpl.class);

    private @Resource BalanceDao balanceDao;
    private @Resource PoiDao poiDao;

    @Override
    public BizPacket recharge(PoiUserData userData, int amount) {
        // TODO 请求一个 '店铺帐户全局分布式锁’

        try {

            // TODO 店铺的帐户需要在帐户认证时预先生成

            PoiAccountData accountData =  poiDao.getAccountData(userData.getPoiId());

            int totalBalance = accountData.getCurBalance() + amount;
            BalanceRechargeRD rd = createRechargeRd(userData,amount,totalBalance);

            balanceDao.addRechargeRd(rd);
            poiDao.updatePoiBalance(amount,userData.getPoiId());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id",rd.getId());
            jsonObject.put("amount",rd.getAmount());
            jsonObject.put("rechargeNo",rd.getRechargeNo());
            jsonObject.put("createTime",rd.getCreateTime());

            return BizPacket.success(jsonObject);
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
    private BalanceRechargeRD createRechargeRd(PoiUserData userData, int amount, int currentTotalBiddingBalance){
        BalanceRechargeRD rd = new BalanceRechargeRD();
        rd.setAmount(amount);
        rd.setAmount(currentTotalBiddingBalance);
        rd.setCreateTime(DateTimeUtil.now());
        rd.setPoiId(userData.getPoiId());
        rd.setUserId(userData.getId());
        rd.setUserName(userData.getName());

        // TODO 等待充值完成后回填
        rd.setRechargeNo("");

        return rd;
    }

    @Override
    public BizPacket getMyRechargeData(PoiUserData userData, int index, int pageSize) {
        try {
            PoiData poiData = poiDao.getPoiData(userData.getPoiId());
            if(poiData == null){
                return BizPacket.error(HttpStatus.FORBIDDEN.value(),"你没有店铺!");
            }


            int total = balanceDao.countRechargeData(poiData.getId());
            if(total <= 0){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"您还没有充过值!");
            }
            List<BalanceRechargeRD> list = balanceDao.getRechargeDataList(poiData.getId(),index * pageSize,pageSize);

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


            int total = balanceDao.countConsumeData(poiData.getId());
            if(total <= 0){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"暂时还没有消费记录!");
            }
            List<BalanceConsumeRd> list = balanceDao.getConsumeDataList(poiData.getId(),index * pageSize,pageSize);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total",total);
            jsonObject.put("list",list);
            return BizPacket.success(jsonObject);
        } catch (Exception e) {
            logger.error("usr="+userData+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }
}
