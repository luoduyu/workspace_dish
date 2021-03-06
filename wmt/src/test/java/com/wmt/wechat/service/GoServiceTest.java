package com.wmt.wechat.service;

import com.wmt.wechat.form.yunying.ShenQingForm;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-03 13:06
 * @version 1.0
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class GoServiceTest {
    //private @Resource  GoService goService;
   // private @Resource PoiUserDao poiUserDao;

//    @Test
//    public void testFormSubmit(){
//        ShenQingForm form = build();
//
//        PoiUserData userData = poiUserDao.getPOIUserDataById("c226527e25c5425ea95d9340486cf2d9");
//        BizPacket packet = goService.requestFormSubmit(form,userData, ShenQingType.KAIDIAN);
//        System.out.println(packet);
//    }

    private static ShenQingForm build(){
        ShenQingForm form = new ShenQingForm();
        form.setAddress("张自忠路88号");
        form.setAmount(1);
        form.setBrandName("一食一");
        form.setCity("北京");
        form.setDishCateId(3);
        form.setDistricts("朝阳");
        form.setPlatform("1");
        form.setPoiType(2);
        form.setProvince("直辖市");
        return form;
    }
}