package com.amt.wechat.dao.globalsetting;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-12 13:19
 * @version 1.0
 */
@Repository("globalSettingDao")
@Mapper
public interface GlobalSettingDao {


    /**
     * 已续费商户数
     * @return
     */
    @Select("SELECT member_num_hasbeen_fee FROM globalsetting LIMIT 1")
    public Integer getMemberNum4HasbeenFee();
}
