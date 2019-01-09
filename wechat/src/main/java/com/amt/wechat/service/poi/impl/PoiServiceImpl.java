package com.amt.wechat.service.poi.impl;

import com.amt.wechat.dao.dish.DishDao;
import com.amt.wechat.dao.poi.PoiDao;
import com.amt.wechat.dao.poi.PoiMaterialDao;
import com.amt.wechat.domain.id.Generator;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.DateTimeUtil;
import com.amt.wechat.form.basic.BasicSettingForm;
import com.amt.wechat.model.poi.MaterialData;
import com.amt.wechat.model.poi.PoiData;
import com.amt.wechat.model.poi.PoiUserData;
import com.amt.wechat.service.poi.PoiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;


@Service("poiService")
public class PoiServiceImpl implements PoiService {
    private static Logger logger = LoggerFactory.getLogger(PoiServiceImpl.class);

    private @Resource PoiMaterialDao poiMaterialDao;
    private @Resource PoiDao poiDao;
    private @Resource DishDao dishDao;

    @Override
    public List<MaterialData> getPoiMaterialDataList() {
        return poiMaterialDao.getPoiMaterialDataList();
    }

    @Override
    public BizPacket updatePoi(PoiUserData userData,BasicSettingForm form) {
        try {
            PoiData poiData = poiDao.getPoiData(userData.getPoiId());
            if(poiData == null){
                return BizPacket.error(HttpStatus.NOT_FOUND.value(),"店铺不存在！");
            }

            if(!StringUtils.isEmpty(form.getPoiBrandName())){
                poiData.setBrandName(form.getPoiBrandName());
            }

            if(form.getPoiCateId() <= 0) {
                return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "非法的经营品类!cateId=" + form.getPoiCateId());
            }
            int r = dishDao.countByCateId(form.getPoiCateId());
            if(r <= 0){
                return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"非法的经营品类!cateId="+form.getPoiCateId());
            }
            poiData.setCateId(form.getPoiCateId());

            if(!StringUtils.isEmpty(form.getPoiProvince())){
                poiData.setProvince(form.getPoiProvince());
            }
            if(!StringUtils.isEmpty(form.getPoiCity())){
                poiData.setCity(form.getPoiCity());
            }
            if(!StringUtils.isEmpty(form.getPoiDistricts())){
                poiData.setDistricts(form.getPoiDistricts());
            }
            if(!StringUtils.isEmpty(form.getPoiStreet())){
                poiData.setStreet(form.getPoiStreet());
            }
            if(!StringUtils.isEmpty(form.getPoiAddress())){
                poiData.setAddress(form.getPoiAddress());
            }

            poiData.setUpdTime(DateTimeUtil.now());

            poiDao.updatePoiData(poiData);

            return BizPacket.success();
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return BizPacket.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage());
        }
    }


    //poiDao.addPoiData(poiData);

    /**
     * 创建一个缺省的店铺数据
     * @return
     */
    private PoiData defaultPoiData(){
        PoiData poiData = new PoiData();
        poiData.setId(Generator.uuid());
        poiData.setName("");
        poiData.setCountry("中国");
        poiData.setProvince("");
        poiData.setCity("");
        poiData.setStreet("");
        poiData.setAddress("");

        poiData.setBrandName("");
        poiData.setCateId(-1);

        poiData.setAccountName("");
        poiData.setAccountPassword("");
        poiData.setEleShopId("");
        poiData.setMtAppAuthToken("");

        poiData.setCreateTime(DateTimeUtil.now());
        poiData.setUpdTime(poiData.getUpdTime());
        return poiData;
    }
}