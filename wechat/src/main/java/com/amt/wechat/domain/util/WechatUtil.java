package com.amt.wechat.domain.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amt.wechat.common.Constants;
import com.amt.wechat.domain.PhoneData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-15 17:46
 * @version 1.0
 */
public class WechatUtil {
    private static Logger logger = LoggerFactory.getLogger(WechatUtil.class);


    /**
     * 获取登录凭证信息
     * @param wxCode
     * @return
     */
    public static JSONObject getSessionKeyOrOpenId(String wxCode){
        String url = String.format(Constants.URL_TEMPLATE, Constants.WEICHAT_APP_ID,Constants.AppSecret,wxCode);
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            logger.info("wxCode={},登录凭证校验失败! ex={}",wxCode,e.getMessage(),e);
            return null;
        }
        String resultText = document.text();
        JSONObject json = JSON.parseObject(resultText);

        Integer errcode = json.getInteger("errcode");
        if(errcode != null && errcode != 0){
            logger.info("登录凭证校验失败!errcode={},errmsg={}",errcode,json.getString("errmsg"));
            return null;
        }
        return json;
    }



    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/CBC/NoPadding";

    /**
     * 生成密钥
     * @return
     * @throws Exception
     */
    private static byte[] generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(128);
        SecretKey key = keyGenerator.generateKey();
        return key.getEncoded();
    }

    /**
     * 生成iv
     * @return
     * @throws Exception
     */
    private static AlgorithmParameters generateIV() throws Exception {
        // iv 为一个 16 字节的数组，这里采用和 iOS 端一样的构造方法，数据全为0
        byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 0x00);

        return generateIV(iv);
    }

    /**
     * 生成iv
     * @param iv
     * @return
     * @throws Exception
     */
    private static AlgorithmParameters generateIV(byte[] iv) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance(KEY_ALGORITHM);
        params.init(new IvParameterSpec(iv));
        return params;
    }

    /**
     * 转化成JAVA的密钥格式
     * @param keyBytes
     * @return
     * @throws Exception
     */
    private static Key convertToKey(byte[] keyBytes) throws Exception {
        SecretKey secretKey = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        return secretKey;
    }

    /**
     * 加密
     */
    private static byte[] encrypt(byte[] data, byte[] keyBytes, AlgorithmParameters iv) throws Exception {
        // 转化为密钥
        Key key = convertToKey(keyBytes);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(data);
    }

    /**
     * 解密
     */
    private static byte[] decrypt(byte[] encryptedData, byte[] keyBytes, AlgorithmParameters iv) throws Exception {
        Key key = convertToKey(keyBytes);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        // 设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(encryptedData);
    }

    /**
     *
     * 解密用户敏感数据获取用户信息:
     *{
     *   "openId": "OPENID",
     *   "nickName": "NICKNAME",
     *   "gender": GENDER,
     *   "city": "CITY",
     *   "province": "PROVINCE",
     *   "country": "COUNTRY",
     *   "avatarUrl": "AVATARURL",
     *   "unionId": "UNIONID",
     *   "watermark": {
     *     "appid": "APPID",
     *     "timestamp": TIMESTAMP
     *   }
     * }
     * 解密 for 用户信息
     * @param encryptedData
     * @param session_key
     * @param iv
     * @return
     */
    public static JSONObject getUserInfo(String encryptedData,String session_key,String iv){
        return decode(encryptedData,session_key,iv);
    }

    /**
     *
     *
     * @param session_key 数据进行加密签名的密钥
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv 加密算法的初始向量
     * @return
     */
    private static JSONObject decode(String encryptedData,String session_key,String iv){
        java.util.Base64.Decoder decoder = Base64.getDecoder();
        try {
            byte[] bytData = decoder.decode(encryptedData);
            byte[] bytSessionkey =decoder.decode(session_key);
            byte[] bytIV = decoder.decode(iv);

            byte[] result = decrypt(bytData, bytSessionkey,generateIV(bytIV));
            String strResult = new String(result,"UTF-8");

            JSONObject json = JSON.parseObject(strResult);

            return json;
        } catch (Exception e) {
           logger.error(e.getMessage(),e);
        }
        return null;
    }

    public static PhoneData getPhoneData(String encryptedData,String session_key,String iv){

        /*
        {
              "phoneNumber": "13580006666",
              "purePhoneNumber": "13580006666",
              "countryCode": "86",
              "watermark": {
                "appid": "APPID",
                "timestamp": TIMESTAMP
              }
         }
         */
        JSONObject jsonObject = decode(encryptedData,session_key,iv);
        if(jsonObject == null){
            return null;
        }

        String phoneNumber = jsonObject.getString("phoneNumber");
        String purePhoneNumber = jsonObject.getString("purePhoneNumber");
        String countryCode = jsonObject.getString("countryCode");
        if(phoneNumber != null && phoneNumber.trim().length()!= 0){
            if(!phoneNumber.startsWith(countryCode)){
                return new PhoneData(phoneNumber,countryCode);
            }
        }
        return new PhoneData(purePhoneNumber,countryCode);
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

    public static void main(String[] args) {
        /*
        String encryptedData ="dXp+e+upG3at0SGnCBNZtQ+++w/hbuwFo2satyK5qTD4xGI8CCM6PHyIlbejVTPgFKr1PUTJmNQVTBKiKCKWf0VoOr+ReC+S5RdqlE0uD2eX9cTP9Oj+EzRm3AdoveJHLun74doXBxDccFTQuuNsCeJv6e2H8JH1awz3kvadGOPvEnMrDKrA99Vm+JTHZK7hy2wGQvdXOQgzFuK8lA3Ow3BJS4IYdIRNXwm8j1K7jxySyWvlIWuT1eHP3B345NYvQ3HxI23T0JC9WkiP6Cop13I97+nO6pl5RcBfOjLGAc4=";
        String session_key  = "Tm9ZwkjBGdSZ8zmnFlHy8Q==";
        String iv= "rZZxbQgeeQSt0+7qyoyLHA==";

        JSONObject json = getUserInfo(encryptedData,session_key,iv);
        System.out.println(json);
        */
    }
}