package com.amt.wechat.domain.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-17 15:00
 * @version 1.0
 */
public class DateTimeUtil {

    public final static DateTimeFormatter FRIENDLY_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of(ZoneId.SHORT_IDS.get("CTT")));


    /**
     * 获取指定时间的
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String now() {
        LocalDateTime ldt =  LocalDateTime.now();
        return ldt.format(FRIENDLY_DATE_TIME_FORMAT);
    }

    /**
     * 距离当天的23:59:59还有多少秒
     * @return
     */
    public static long interval(){
        LocalTime now = LocalTime.now();
        LocalTime max = LocalTime.of(23,59,59);
        long d2 =  max.toSecondOfDay() - now.toSecondOfDay();
        return d2;
    }

    public static LocalDateTime getTime(String timestamp) {
        LocalDateTime ldt = LocalDateTime.parse(timestamp,FRIENDLY_DATE_TIME_FORMAT);
        return ldt;
    }


    public static void main(String[] args) {
        //getTime("2019-01-10 17:56:03");

        LocalDateTime ldt = LocalDateTime.now().plusYears(1).withHour(23).withMinute(59).withSecond(59).minusDays(1);
        System.out.println(ldt.format(FRIENDLY_DATE_TIME_FORMAT));

        System.out.println(Math.multiplyExact(2, 7));
    }
}