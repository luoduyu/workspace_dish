package com.amt.wechat.domain.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amt.wechat.common.Constants;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.model.poi.PoiUserData;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
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
     *
     * @param wxCode
     * @return
     */
    public static JSONObject getSessionKeyOrOpenId(String wxCode) {
        String url = String.format(Constants.URL_TEMPLATE, Constants.WEICHAT_APP_ID, Constants.WECHAT_APP_SECRET, wxCode);
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            logger.info("wxCode={},登录凭证校验失败! ex={}", wxCode, e.getMessage(), e);
            return null;
        }
        String resultText = document.text();
        JSONObject json = JSON.parseObject(resultText);

        Integer errcode = json.getInteger("errcode");
        if (errcode != null && errcode != 0) {
            logger.info("登录凭证校验失败!errcode={},errmsg={}", errcode, json.getString("errmsg"));
            return null;
        }
        return json;
    }


    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/CBC/NoPadding";

    /**
     * 生成密钥
     *
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
     *
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
     *
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
     *
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
     * 解密用户敏感数据获取用户信息:
     * {
     * "openId": "OPENID",
     * "nickName": "NICKNAME",
     * "gender": GENDER,
     * "city": "CITY",
     * "province": "PROVINCE",
     * "country": "COUNTRY",
     * "avatarUrl": "AVATARURL",
     * "unionId": "UNIONID",
     * "watermark": {
     * "appid": "APPID",
     * "timestamp": TIMESTAMP
     * }
     * }
     * 解密 for 用户信息
     *
     * @param encryptedData
     * @param session_key
     * @param iv
     * @return
     */
    public static JSONObject getUserInfo(String encryptedData, String session_key, String iv) {
        return decode(encryptedData, session_key, iv);
    }

    /**
     * @param session_key   数据进行加密签名的密钥
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv            加密算法的初始向量
     * @return
     */
    private static JSONObject decode(String encryptedData, String session_key, String iv) {
        java.util.Base64.Decoder decoder = Base64.getDecoder();
        try {
            byte[] bytData = decoder.decode(encryptedData);
            byte[] bytSessionkey = decoder.decode(session_key);
            byte[] bytIV = decoder.decode(iv);

            byte[] result = decrypt(bytData, bytSessionkey, generateIV(bytIV));
            String strResult = new String(result, "UTF-8");

            JSONObject json = JSON.parseObject(strResult);

            return json;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static PhoneData getPhoneData(String encryptedData, String session_key, String iv) {

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
        JSONObject jsonObject = decode(encryptedData, session_key, iv);
        if (jsonObject == null) {
            return null;
        }

        String phoneNumber = jsonObject.getString("phoneNumber");
        String purePhoneNumber = jsonObject.getString("purePhoneNumber");
        String countryCode = jsonObject.getString("countryCode");
        if (phoneNumber != null && phoneNumber.trim().length() != 0) {
            if (!phoneNumber.startsWith(countryCode)) {
                return new PhoneData(phoneNumber, countryCode);
            }
        }
        return new PhoneData(purePhoneNumber, countryCode);
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


    /**
     * Copyright (c) 2018 by CANSHU
     * <p>
     * 电话号码
     *
     * @author adu Create on 2018-12-27 14:36
     * @version 1.0
     */
    static class PhoneData implements Serializable {
        private static final long serialVersionUID = 7882043107491655464L;

        /**
         * 临时存储在session中的电话号码(不带区号)
         */
        public transient static final String SESSION_PHONE = "POI_USER_PHONE";

        /**
         * 临时存储在session中的电话号码的区号
         */
        public transient static final String SESSION_PHONE_CC = "POI_USER_PHONE_COUNTRYCODE";


        private String purePhoneNumber;
        private String countryCode;

        public PhoneData(String purePhoneNumber, String countryCode) {
            this.purePhoneNumber = purePhoneNumber;
            this.countryCode = countryCode;
        }

        public String getPurePhoneNumber() {
            return purePhoneNumber;
        }

        public String getCountryCode() {
            return countryCode;
        }

        @Override
        public String toString() {
            return "PhoneData{" +
                    "purePhoneNumber='" + purePhoneNumber + '\'' +
                    ", countryCode='" + countryCode + '\'' +
                    '}';
        }
    }


    public static BizPacket check(PoiUserData user) {
        if (user.getIsAccountNonLocked() != 1) {
            return BizPacket.error(HttpStatus.UNAUTHORIZED.value(), "User account is locked");
        }

        if (user.getIsEnabled() != 1) {
            return BizPacket.error(HttpStatus.UNAUTHORIZED.value(), "User is disabled");
        }

        if (user.getIsAccountNonExpired() != 1) {
            return BizPacket.error(HttpStatus.UNAUTHORIZED.value(), "User account has expired");
        }

        if (user.getIsCredentialsNonExpired() != 1) {
            return BizPacket.error(HttpStatus.UNAUTHORIZED.value(), "User credentials have expired");
        }
        return BizPacket.success();
    }



    /**
     * 有两种失败法:
     * 1)有错误返回标记,通信成功
     * 2)有错误返回标记,通信失败
     *
     * @param response
     * @param flag
     * @param result
     * @throws IOException
     */
    public static void responseFail(HttpServletResponse response, String flag, Map<String,String> result){
        try {
            if(result != null && result.containsKey("return_msg")){
                String xml = failXML(flag,result.get("return_msg"));
                response.getWriter().write(xml);
                return;
            }

            String xml = failXML(flag,"未知错误!");
            response.getWriter().write(xml);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
    }


    public static final String WECHAT_PAY_CALLBACK_SUCC= "<xml><return_code><![CDATA["+Constants.WE_SUCCESS+"]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    public static final String WECHAT_PAY_CALLBACK_FAIL= "<xml><return_code><![CDATA[%s]]></return_code><return_msg><![CDATA[%s]]></return_msg></xml>";

    /**
     * 成功后设置返回的xml
     * @Title: setXML
     * @Description:
     * @param @param return_code
     * @param @param return_msg
     * @param @return
     * @return String
     * @throws
     */
    private static String failXML(String return_code, String return_msg) {
        String xml = String.format(WECHAT_PAY_CALLBACK_FAIL, return_code, return_msg);
        return xml;
    }

    public static String getResponseText(HttpServletRequest request) throws IOException {
        try(BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()))){
            String line = null;
            StringBuilder response = new StringBuilder();
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    /**
     * 将字符串转为XML
     * @param responseText
     * @return
     */
    public static  org.dom4j.Document getResponseXML(String responseText) throws DocumentException {
        org.dom4j.Document doc = DocumentHelper.parseText(responseText);
        return doc;
    }


    public static void main(String[] args) {
        /*
        String encryptedData ="dXp+e+upG3at0SGnCBNZtQ+++w/hbuwFo2satyK5qTD4xGI8CCM6PHyIlbejVTPgFKr1PUTJmNQVTBKiKCKWf0VoOr+ReC+S5RdqlE0uD2eX9cTP9Oj+EzRm3AdoveJHLun74doXBxDccFTQuuNsCeJv6e2H8JH1awz3kvadGOPvEnMrDKrA99Vm+JTHZK7hy2wGQvdXOQgzFuK8lA3Ow3BJS4IYdIRNXwm8j1K7jxySyWvlIWuT1eHP3B345NYvQ3HxI23T0JC9WkiP6Cop13I97+nO6pl5RcBfOjLGAc4=";
        String session_key  = "Tm9ZwkjBGdSZ8zmnFlHy8Q==";
        String iv= "rZZxbQgeeQSt0+7qyoyLHA==";

        JSONObject json = getUserInfo(encryptedData,session_key,iv);
        System.out.println(json);
        */


        /*
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "123456");
        jsonObject.put("goodsName", "i am is goods");
        jsonObject.put("imgUrl", "https://www.wmt.com/a/b/c.jpg");

        String result = JSON.toJSONString(BizPacket.success(jsonObject));
        System.out.println(result);
        */

        int r = roundDown(mul4Float(123,0.1f));
        System.out.println(r);
    }

    public static int roundDown(float v) {
        BigDecimal b = new BigDecimal(Float.toString(v));
        b.setScale(0,BigDecimal.ROUND_DOWN);
        return b.intValue();
    }

    /**
     *
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static float mul4Float(float v1, float v2) {
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.multiply(b2).floatValue();
    }
}