package com.wmt.dlock.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-15 20:34
 * @version 1.0
 */
@Configuration
@AutoConfigureAfter(RedisTemplateConfig.class)
public class RedisTemplateConfig {
    private static Logger logger = LoggerFactory.getLogger(RedisTemplateConfig.class);
    @Bean
    public RedisTemplate<String, Serializable> redisCacheTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Serializable> template = new RedisTemplate<>();

        // k-v
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());

        // hk-hv
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(RedisSerializer.json());

        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    /**
     * 使用默认的工厂初始化redis操作模板
     * @param connectionFactory
     * @return
     */
    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {

        if(connectionFactory != null){
            JedisConnectionFactory jc = (JedisConnectionFactory)connectionFactory;
            logger.info("=======>>> dlock.redis.hostName="+jc.getHostName()+",dlock.redis.port="+jc.getPort());
        }

        StringRedisTemplate template =  new StringRedisTemplate(connectionFactory);
        return template;
    }
}