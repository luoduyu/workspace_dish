package com.wmt.dlock.config;


import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
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


//    @Bean
//    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate redisTemplate = new RedisTemplate();
//
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//        GenericJackson2JsonRedisSerializer redisSerializer = new GenericJackson2JsonRedisSerializer();
//
//        redisTemplate.setDefaultSerializer(redisSerializer);
//
//        return redisTemplate;
//    }


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
        return new StringRedisTemplate(connectionFactory);
    }
}