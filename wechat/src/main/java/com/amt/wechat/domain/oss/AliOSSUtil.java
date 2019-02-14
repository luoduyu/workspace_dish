package com.amt.wechat.domain.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.LifecycleRule;
import com.aliyun.oss.model.SetBucketLifecycleRequest;
import com.amt.wechat.domain.util.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Copyright (c) 2019 by CANSHU
 *  阿里OSS工具类
 *
 * @author adu Create on 2019-02-12 11:40
 * @version 1.0
 */
public class AliOSSUtil {
    private static Logger logger = LoggerFactory.getLogger(AliOSSUtil.class);

    /**
     * 外网访问
     */
    private static final String endpoint_external = "oss-cn-beijing.aliyuncs.com";

    /**
     * ECS 的经典网络访问(内网)
     */
    private static final String endpoint_internal = "oss-cn-beijing-internal.aliyuncs.com";
    /**
     * 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维,
     * 请登录 https://ram.console.aliyun.com 创建RAM账号。
     */
    private static final String accessKeyId = "LTAIPMOvSoMv0AoX";
    private static final String accessKeySecret = "Z7jfgB9S4JWZjnDjAqZXK6xnZV09Bg";

    /**
     * 专供二维码存储的bucket
     */
    private static final String bucketName = "wmt-wxacode";

    /**
     * 微信图片的有效期:30天
     */
    public static final int LIFECYCLE_WX_CODE_IMG=30;


    /**
     *
     * @param userId 用户Id
     * @param shareUrl 分享的URL
     * @param imgContent 图片内容
     * @return 图片地址
     */
    public static String putWXACode(String userId, String shareUrl, InputStream imgContent){
        String objectName = getObjectName(userId,shareUrl);

        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint_internal, accessKeyId, accessKeySecret);
        SetBucketLifecycleRequest request = new SetBucketLifecycleRequest(bucketName);

        // 设置规则ID和文件前缀。
        String ruleId0 = "rule0";
        String matchPrefix0 = "*";

        // 距最后修改时间30天后过期
        request.AddLifecycleRule(new LifecycleRule(ruleId0, matchPrefix0, LifecycleRule.RuleStatus.Enabled, LIFECYCLE_WX_CODE_IMG));

        // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称(objectName)
        ossClient.putObject(bucketName, objectName, imgContent);

        // 关闭OSSClient。
        ossClient.shutdown();

        String url = new StringBuilder("https://").append(bucketName).append(".").append(endpoint_external).append("/").append(objectName).append(".png").toString();
        logger.info("上传到oss成功!url={}",url);
        return url;
    }

    private static String getObjectName(String userId,String shareUrl){
        String objectName = WechatUtil.getMD5(userId+"-"+shareUrl);

        // 出错时再调用一次
        if(objectName == null || objectName.length()==0){
            objectName = WechatUtil.getMD5(userId+"-"+shareUrl);
        }
        return objectName;
    }
}