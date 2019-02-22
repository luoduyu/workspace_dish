package com.wmt.commons.domain.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;


/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author  adu Create on 2018-12-06 20:01
 * @version 1.0
 */
public class Generator {
    private static Logger logger = LoggerFactory.getLogger(Generator.class);

    private static final String IP_ADDRESS;
    private static int counter = 0;
    private static DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");

    static {
        int ipadd;
        try {
            ipadd = toInt(getLocalHost());
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

    /**
     * 订单号
     * @return
     */
    public static String generate() {
        return new StringBuilder(20)
                .append(df.format(new Date()))
                .append(IP_ADDRESS)
                .append(format(getCount())).toString();
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


    private static InetAddress getLocalHostLanAddress() {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();

                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {

                        // 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr;

                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            return jdkSuppliedAddress;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    private static InetAddress _inetAddress = null;


    /**
     * 拿到本地的IP地址
     * @return
     */
    public static byte[] getLocalHost(){
        if(_inetAddress != null){
            return _inetAddress.getAddress();
        }

        InetAddress inetAddress =  getLocalHostLanAddress();
        if(inetAddress != null){
            _inetAddress = inetAddress;
            return inetAddress.getAddress();
        }
        return "127.0.0.1".getBytes();
    }

    public static String inetAddressToText(){
        byte[] localHost = getLocalHost();
        return (localHost[0] & 0xff) + "." + (localHost[1] & 0xff) + "." + (localHost[2] & 0xff) + "." + (localHost[3] & 0xff);
    }
    public static void main(String[] args) {
        System.out.println(uuid());
        System.out.println(generate());
    }
}