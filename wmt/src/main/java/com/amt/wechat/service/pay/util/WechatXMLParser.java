package com.amt.wechat.service.pay.util;

import com.amt.wechat.common.constants.Constants;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-22 11:18
 * @version 1.0
 */
public class WechatXMLParser {
    private static final Logger logger = LoggerFactory.getLogger(WechatXMLParser.class);


    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     *
     * @param strxml
     * @return
     */
    public static Map<String, String> doXMLParse(String strxml) throws JDOMException, IOException {
        if (null == strxml || "".equals(strxml)) {
            return null;
        }

        Map<String, String> m = new HashMap<String, String>();
        InputStream in = new ByteArrayInputStream(strxml.getBytes());

        Document doc = new SAXBuilder().build(in);
        Element root = doc.getRootElement();
        List<Element> list = root.getChildren();
        Iterator<Element> it = list.iterator();
        while (it.hasNext()) {
            Element e = it.next();
            String k = e.getName();
            String v = "";
            List<Element> children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }

        //关闭流
        in.close();

        return m;
    }

    /**
     * 获取子结点的xml
     *
     * @param children
     * @return String
     */
    private static String getChildrenText(List<Element> children) {

        StringBuffer sb = new StringBuffer();
        Iterator<Element> it = children.iterator();
        while (it.hasNext()) {
            Element e = it.next();
            String name = e.getName();
            String value = e.getTextNormalize();

            List<Element> list = e.getChildren();
            sb.append("<" + name + ">");
            if (!list.isEmpty()) {
                sb.append(getChildrenText(list));
            }
            sb.append(value);
            sb.append("</" + name + ">");
        }

        return sb.toString();
    }

    public static final String ORDER_ID ="orderId";

    /**
     *  微信支付回调重要参数定义;
     *  k:微信方参数名
     *  v:'我'方参数名
     */
    public static final Map<String,String> wechatPayCallbackParams = new HashMap<>();
    static{
        wechatPayCallbackParams.put("attach","attach");

        // 商户订单号
        wechatPayCallbackParams.put("out_trade_no",ORDER_ID);

        // 总金额
        wechatPayCallbackParams.put("total_fee","amount");

        // 微信支付订单号
        wechatPayCallbackParams.put("transaction_id","transactionId");

        // 是否关注公众账号
        wechatPayCallbackParams.put("is_subscribe","isWechatSubscribe");

        // 付款银行代号
        wechatPayCallbackParams.put("bank_type","bankType");

        // 支付完成时间
        wechatPayCallbackParams.put("time_end","timeEnd");

        // 货币种类
        wechatPayCallbackParams.put("fee_type","currencyType");

        // 现金支付金额
        wechatPayCallbackParams.put("cash_fee","cashFee");
    }


    /**
     * 提取一部分额外信息作为摘要
     * @param wechatPayCallbackParams
     * @return
     */
    public static String joinSummary(Map<String,String> wechatPayCallbackParams){
        StringBuilder buffer = new StringBuilder();

        if(wechatPayCallbackParams.containsKey("isWechatSubscribe")){
            buffer.append(",isWechatSubscribe=").append(wechatPayCallbackParams.get("isWechatSubscribe"));
        }

        if(wechatPayCallbackParams.containsKey("bankType")){
            buffer.append(",bankType=").append(wechatPayCallbackParams.get("bankType"));
        }

        if(wechatPayCallbackParams.containsKey("currencyType")){
            buffer.append(",currencyType=").append(wechatPayCallbackParams.get("currencyType"));
        }

        if(wechatPayCallbackParams.containsKey("cashFee")){
            buffer.append(",cashFee=").append(wechatPayCallbackParams.get("cashFee"));
        }
        if(buffer.charAt(0) == ','){
            buffer.deleteCharAt(0);
        }

        // isWechatSubscribe=N,bankType=CMB_DEBIT,currencyType=CNY,cashFee=1
        //logger.info("buffer == >>>{}",buffer.toString());
        return buffer.toString();
    }

    /**
     * 支付回调结果参数解析
     * @param root
     * @return
     */
    public static Map<String,String> callbackResultParse(org.dom4j.Element root){

        Map<String,String> result = new HashMap<>();

        // 业务结果
        String result_code = root.elementTextTrim("result_code");
        result.put("result_code",result_code);


        String return_msg = root.elementTextTrim("return_msg");
        if(!StringUtils.isEmpty(return_msg)){
            result.put("return_msg",return_msg);
        }
        if (!Constants.WE_SUCCESS.equals(result_code)) {
            return result;
        }


        for(Map.Entry<String,String> entry:wechatPayCallbackParams.entrySet()){
            String value = root.elementTextTrim(entry.getKey());
            if(!StringUtils.isEmpty(value)){
                result.put(entry.getValue(),value);
            }
        }
        return result;
    }
}