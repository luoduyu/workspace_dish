package com.amt.wechat.domain.redis;

import com.amt.wechat.service.redis.RedisService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.Serializable;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-20 13:29
 * @version 1.0
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisCacheAutoConfiguration {
    @Bean
    public RedisTemplate<String, Serializable> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
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



    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(RedisConstants.REDIS_CH_BALANCE_SETTING));
        return container;
    }

    /**
     * 利用反射来创建监听到消息之后的执行方法
     * @param redisReceiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter(RedisService redisReceiver) {
        return new MessageListenerAdapter(redisReceiver, RedisConstants.REDIS_CH_BALANCE_SETTING);
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

