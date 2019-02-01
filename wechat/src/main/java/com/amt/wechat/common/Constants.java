package com.amt.wechat.common;

public class Constants {

    /**
     * 微信小程序APPID
     */
    public static final String WEICHAT_APP_ID="wx35acf287e0fc1de7";

    /**
     * 微信小程序应用密钥
     */
    public static final String WECHAT_APP_SECRET ="e9ace1f5c65d12a4e50a6442efb99708";


    /**
     * 餐数科技的API密钥
     */
    public static final String WECHAT_API_KEY ="waimaitongzhulipingzhishanghu121";

    /**
     * 微信支付分配的商户号
     */
    public static final String WECHAT_MCH_ID ="1521992721";

    /** 微信预支付订单地址 */
    public static final String WECHAT_PREORDERURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";


    /**
     * 微信登录凭证校验接口
     */
    public static final  String URL_TEMPLATE = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";


    /**
     * 微信小程序全局唯一后台接口调用凭据
     */
    public static final  String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+WEICHAT_APP_ID+"&secret="+WECHAT_APP_SECRET;


    /**
     * 微信小程序二维码图片生成
     */
    public static final  String URL_WX_ACODE_UNLIMIT = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";


    /**
     * 已登录的
     */
    public static final String  WECHAT_LOGGED_USER  = "wechat_logged_user";
    public static final String REQ_PARAM_ACCESSTOKEN = "accessToken";



    /**
     * 支付回调类前缀
     */
    public  static final String PAY_PREFIX="/wxpay";

    /**
     * 竞价充值回调
     */
    public  static final String PAY_CALLBACK_URL_BIDDING=PAY_PREFIX + "/bidding/recharge/in/callback";
    public static final String PAY_CALLBACK_URL_ALL_BIDDING = "https://www.waimaitong.xin"+PAY_CALLBACK_URL_BIDDING;

    public  static final String PAY_CALLBACK_URL_BALANCE=PAY_PREFIX + "/balance/recharge/in/callback";
    public static final String PAY_CALLBACK_URL_ALL_BALANCE = "https://www.waimaitong.xin"+PAY_CALLBACK_URL_BALANCE;

    public  static final String PAY_CALLBACK_URL_MEMBER_BUY=PAY_PREFIX + "/member/buy/callback";
    public static final String PAY_CALLBACK_URL_ALL_MEMBER_BUY = "https://www.waimaitong.xin"+PAY_CALLBACK_URL_MEMBER_BUY;


    public  static final String PAY_CALLBACK_URL_ORDER=PAY_PREFIX + "/order/pay/callback";
    public static final String PAY_CALLBACK_URL_ALL_ORDER = "https://www.waimaitong.xin"+PAY_CALLBACK_URL_ORDER;

    public static final String WE_SUCCESS ="SUCCESS";
    public static final String WE_FAIL ="FAIL";
}