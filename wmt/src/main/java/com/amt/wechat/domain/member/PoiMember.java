package com.amt.wechat.domain.member;

import com.amt.wechat.domain.util.DateTimeUtil;
import com.amt.wechat.model.poi.PoiMemberData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *
 * @author adu Create on 2019-02-14 09:54
 * @version 1.0
 */
public class PoiMember {
    private static final Logger logger = LoggerFactory.getLogger(PoiMember.class);


    /**
     * 指定店铺是否会员
     * @param memberData
     * @return
     */
    public static boolean isMember(PoiMemberData memberData){
        if(memberData == null){
            return false;
        }
        try {
            int flag = DateTimeUtil.getDateTime(memberData.getExpiredAt()).compareTo(LocalDateTime.now());
            if(flag <= 0 ){
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("expiredAt="+memberData.getExpiredAt()+",e="+e.getMessage(),e);
            return false;
        }
    }
}
