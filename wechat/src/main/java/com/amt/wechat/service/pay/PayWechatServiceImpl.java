package com.amt.wechat.service.pay;

import com.amt.wechat.common.Constants;
import com.amt.wechat.common.WechatTradeType;
import com.amt.wechat.domain.id.Generator;
import com.amt.wechat.domain.packet.BizPacket;
import com.amt.wechat.domain.util.ZXHttpClient;
import com.amt.wechat.service.pay.util.MD5Util;
import com.amt.wechat.service.pay.util.WechatXMLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service("payWechatService")
public class PayWechatServiceImpl implements PayWechatService {
    private static Logger logger = LoggerFactory.getLogger(PayWechatServiceImpl.class);

    @Override
    public BizPacket prePayOrder(String openId,String nonce_str, String body, String attach, String orderId, int amount, String notifyCallbackUrl) {

        logger.info("充值支付参数nonce_str={},body={},attach={},orderId={}, amount={}, notifyCallbackUrl={}, openId={}",nonce_str,body,attach, orderId, amount, notifyCallbackUrl, openId);

        // 组装SIGN参数
        SortedMap<String, String> map = this.buildParams(nonce_str,body,attach, orderId, amount, notifyCallbackUrl, WechatTradeType.JSAPI,openId);


        // 创建sign
        String sign = makeSign(map);
        map.put("sign", sign);

        // 组装支付XML
        String xml = toXml(map);
        logger.info("xml={}",xml);

        // 请求
        String packet;
        try {
            packet = ZXHttpClient.postXMLStr(Constants.WECHAT_PREORDERURL, xml);
        } catch (Exception e) {
            logger.error("openId="+openId+",nonce="+nonce_str+",body="+body+",attach="+attach+",orderNo="+orderId+",amount="+amount+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"统一预下单时遇到错误:e="+e.getMessage());
        }

        if (StringUtils.isEmpty(packet)) {
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "统一预下单时未能获得返回结果!");
        }

        Map<String, String> resultMap;
        try {
            resultMap = WechatXMLParser.doXMLParse(packet);
        } catch (Exception e) {
            logger.error("openId="+openId+",nonce="+nonce_str+",body="+body+",attach="+attach+",orderNo="+orderId+",amount="+amount+",e="+e.getMessage(),e);
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"解析统一预下单返回结果时遇到错误:e="+e.getMessage());
        }
        if (resultMap == null || resultMap.isEmpty()) {
            return BizPacket.error(HttpStatus.BAD_REQUEST.value(), "解析统一预下单时的返回结果为空!");
        }


        String resultCode = resultMap.get("result_code");
        if (Constants.WE_SUCCESS.equals(resultCode)) {
            String prepay_id = resultMap.get("prepay_id");
            return BizPacket.success(prepay_id);
        }
        return BizPacket.error(HttpStatus.BAD_REQUEST.value(),"遇到其它错误:"+resultMap.get("return_msg"));
    }


    /**
     * 组装支付XML
     * @param treeMap
     * @return
     */
    private String toXml(SortedMap<String, String> treeMap) {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {

            //if ("attach".equalsIgnoreCase(entry.getKey()) || "body".equalsIgnoreCase(entry.getKey()) || "sign".equalsIgnoreCase(entry.getKey())) {
            //if("attach".equalsIgnoreCase(entry.getKey()) || "body".equalsIgnoreCase(entry.getKey())) {
            //    xml.append("<" + entry.getKey() + "><![CDATA[").append(entry.getValue()).append("]]></" + entry.getKey() + ">");
            //} else {
                xml.append("<" + entry.getKey() + ">").append(entry.getValue()).append("</" + entry.getKey() + ">");
            //}
        }
        xml.append("</xml>");
        return xml.toString();
    }


    /**
     * 签名
     * @param paraMap
     * @return
     */
    private String makeSign(SortedMap<String, String> paraMap) {
        // 第二步，在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，
        // 再将得到的字符串所有字符转换为大写，得到sign值signValue。(签名)

        StringBuilder _sign = formatUrlMap(paraMap);
        //logger.info("连接字符串={}",_sign.toString());

        _sign.append("key=").append(Constants.WECHAT_API_KEY);
        //logger.info("带API KEY 的连接字符串={}",_sign.toString());

        String sign = MD5Util.MD5Encode(_sign.toString(),"UTF-8").toUpperCase();
        //logger.info("签名={}",sign);
        return sign;
    }


    /**
     * 组装预支付订单
     *
     * @param nonce_str 随机字符串,长度要求在32位以内,推荐随机数生成算法
     * @param body 商品简单描述
     * @param attach  =userId+'_'+RechargeType+'_'+channel
     * @param orderId  商户方生成的订单号
     * @param amount  支付金额,单位:分
     * @param notifyCallbackUrl	支付回调URL
     * @param weTradeType	微信交易类型
     * @return
     */
    private SortedMap<String, String> buildParams(String nonce_str, String body, String attach, String orderId, Integer amount,String notifyCallbackUrl, WechatTradeType weTradeType,String openId) {
        SortedMap<String, String> paramMap = new TreeMap<String, String>();
        paramMap.put("appid", Constants.WEICHAT_APP_ID);
        paramMap.put("mch_id", Constants.WECHAT_MCH_ID);
        paramMap.put("nonce_str", nonce_str);
        paramMap.put("body", body);
        paramMap.put("attach", attach);


        paramMap.put("out_trade_no", orderId);
        paramMap.put("total_fee", amount.toString());

        // 调用微信支付API的机器IP,支持IPV4和IPV6两种格式的IP地址
        paramMap.put("spbill_create_ip", Generator.inetAddressToText());
        paramMap.put("notify_url", notifyCallbackUrl);
        paramMap.put("trade_type", weTradeType.key());
        paramMap.put("openid", openId);
        return paramMap;
    }


    /**
     *
     * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br>
     * 实现步骤: <br>
     *
     * @param paraMap   要排序的Map对象
     * @return
     */
    private static StringBuilder formatUrlMap(Map<String, String> paraMap){
        Map<String, String> tmpMap = paraMap;
        try
        {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());

            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>()
            {
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2)
                {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });



            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds){
                String key = item.getKey();
                String val = item.getValue();

                if (!StringUtils.isEmpty(val) && !"sign".equals(key) && !"key".equals(key)) {
                    buf.append(key + "=" + val).append("&");
                }

            }
            return buf;
        } catch (Exception e){
            return null;
        }
    }
}
