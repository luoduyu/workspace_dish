package com.amt.wechat.domain.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-17 15:00
 * @version 1.0
 */
public class DateTimeUtil {

    private final static DateTimeFormatter FRIENDLY_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    /**
     * 获取指定时间的
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String now() {
        LocalDateTime ldt =  LocalDateTime.now();
        return ldt.format(FRIENDLY_DATE_TIME_FORMAT);
    }
}
