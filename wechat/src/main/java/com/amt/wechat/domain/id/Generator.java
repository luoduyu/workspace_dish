package com.amt.wechat.domain.id;

import java.security.SecureRandom;
import java.util.Date;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.UUID;


/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author  adu Create on 2018-12-06 20:01
 * @version 1.0
 */
public class Generator {

    private static final String IP_ADDRESS;

    private static int counter = 0;

    private static DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");

    static {
        int ipadd;
        try {
            ipadd = toInt(InetAddress.getLocalHost().getAddress());
        } catch (Exception e) {
            ipadd = 0;
        }
        String str = Integer.toString(ipadd);
        StringBuilder buf = new StringBuilder("0000000000");
        IP_ADDRESS = buf.replace(10 - str.length(), 10, str).substring(5);
    }

    private static int getCount() {
        synchronized (Generator.class) {
            return counter > 999 ? counter = 0 : ++counter;
        }
    }

    private static StringBuilder format(int value) {
        String str = Integer.toString(value);
        StringBuilder buf = new StringBuilder("000");
        buf.replace(3 - str.length(), 3, str);
        return buf;
    }

    private static int toInt(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
        }
        return result;
    }

    public static String generate() {
        return new StringBuilder(20).append(df.format(new Date()))
                .append(IP_ADDRESS).append(format(getCount())).toString();
    }


    public static String uuid(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }


    /**
     * 获取验证码数字
     */
    public static String generateCode(){
        SecureRandom rand = new SecureRandom();
        StringBuilder buffer = new StringBuilder(4);

        for (int i = 0; i < 6; i++) {
            buffer.append(rand.nextInt(10));
        }
        return buffer.toString();
    }
    public static void main(String[] args) {
        for(int i=0;i<5;i++) {
            System.out.println(uuid());
        }
    }
}