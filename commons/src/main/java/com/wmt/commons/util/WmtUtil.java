package com.wmt.commons.util;

import com.wmt.commons.domain.packet.BizPacket;
import org.apache.http.HttpStatus;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-20 13:13
 * @version 1.0
 */
public class WmtUtil {

    /**
     * 获取String的MD5值
     *
     * @param info 字符串
     * @return 该字符串的MD5值
     */
    public static String getMD5(String info) {
        try {
            //获取 MessageDigest 对象，参数为 MD5 字符串，表示这是一个 MD5 算法（其他还有 SHA1 算法等）：
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            // update(byte[])方法，输入原数据
            // 类似StringBuilder对象的append()方法，追加模式，属于一个累计更改的过程
            md5.update(info.getBytes("UTF-8"));

            // digest()被调用后,MessageDigest对象就被重置，即不能连续再次调用该方法计算原数据的MD5值。可以手动调用reset()方法重置输入源。
            // digest()返回值16位长度的哈希值，由byte[]承接
            byte[] md5Array = md5.digest();

            // byte[]通常我们会转化为十六进制的32位长度的字符串来使用,本文会介绍三种常用的转换方法
            return bytesToHex1(md5Array);

        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    private static String bytesToHex1(byte[] md5Array) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < md5Array.length; i++) {

            // 注意此处为什么添加 0xff & ?
            int temp = 0xff & md5Array[i];
            String hexString = Integer.toHexString(temp);

            // 如果是十六进制的0f，默认只显示f，此时要补上0
            if (hexString.length() == 1) {
                strBuilder.append("0").append(hexString);
            } else {
                strBuilder.append(hexString);
            }
        }
        return strBuilder.toString();
    }


    /**
     * 验证手机号码是否合法
     *
     * @param mobiles
     * @return false:非法;true:合法
     */
    public static boolean isMobileNO(String mobiles) {
        boolean flag = false;
        try {
            // Pattern p = Pattern.compile("^((17[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,3,5-9]))\\d{8}$");
            Pattern p = Pattern.compile("^((17[0-9])|(13[0-9])|(14[5,7])|(15[0-9])|(16[0-9])|(18[0-9])|(19[0-9]))\\d{8}$");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static BizPacket check(int isAccountNonLocked, int isEnabled, int isAccountNonExpired, int isCredentialsNonExpired) {
        if (isAccountNonLocked != 1) {
            return BizPacket.error(HttpStatus.SC_UNAUTHORIZED, "User account is locked");
        }

        if (isEnabled != 1) {
            return BizPacket.error(HttpStatus.SC_UNAUTHORIZED, "User is disabled");
        }

        if (isAccountNonExpired != 1) {
            return BizPacket.error(HttpStatus.SC_UNAUTHORIZED, "User account has expired");
        }

        if (isCredentialsNonExpired != 1) {
            return BizPacket.error(HttpStatus.SC_UNAUTHORIZED, "User credentials have expired");
        }
        return BizPacket.success();
    }
}
