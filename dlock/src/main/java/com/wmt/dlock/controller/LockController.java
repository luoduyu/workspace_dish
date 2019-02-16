package com.wmt.dlock.controller;

import com.wmt.dlock.lock.DistributedLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.atomic.LongAdder;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-02-15 20:33
 * @version 1.0
 */
@RestController
public class LockController {
    private static final Logger logger = LoggerFactory.getLogger(LockController.class);

    private static LongAdder longAdder = new LongAdder();
    private static Long ACQUIRE_TIMEOUT_IN_MILLIS = (long) Integer.MAX_VALUE;

    private static Long stock = 100000L;
    private @Resource StringRedisTemplate stringRedisTemplate;
    private final DistributedLock lock;
    static {
        longAdder.add(stock);
    }

    @Autowired
    public LockController(@Autowired StringRedisTemplate stringRedisTemplate){
        lock = new DistributedLock(stringRedisTemplate,"seckillV2:" + uuid());
    }

    @GetMapping("/lock/seckill")
    public String seckillV2() throws InterruptedException {
        boolean acquireResult = false;
        try {

            acquireResult = lock.acquire(ACQUIRE_TIMEOUT_IN_MILLIS);
            if (!acquireResult) {
                return "人太多了，换个姿势操作一下!";
            }
            if (longAdder.longValue() == 0L) {
                return "已抢光!";
            }

            doSomeThing();
            longAdder.decrement();
            logger.info("已抢: " + (stock - longAdder.longValue()) + ", 还剩下: " + longAdder.longValue());

        } finally {
            if (acquireResult) {
                boolean releaseResult = lock.release();
                logger.error("释放锁{}！",releaseResult?"成功":"失败");
            }
        }

        return "OK";
    }

    private void doSomeThing() {
        try {
            long ret = stringRedisTemplate.opsForValue().increment("already_bought");
            logger.info("doSomeThing={}",ret);
        } catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }

    private static String uuid(){
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }


    private static final String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    @GetMapping("/redis")
    public String test(){
        stringRedisTemplate.opsForValue().setIfAbsent("ikey", "uuid");

        DefaultRedisScript<Integer> redisScript =  new DefaultRedisScript<>();
        redisScript.setScriptText(luaScript);
        redisScript.setResultType(Integer.class);

        Object object = stringRedisTemplate.execute(redisScript,Collections.singletonList("ikey"), "uuid");
        System.out.println("object="+object);


//        stringRedisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
//            RedisAsyncCommandsImpl cmd = (RedisAsyncCommandsImpl) redisConnection.getNativeConnection();
//
//            Object result = cmd.eval(luaScript, ScriptOutputType.VALUE, Collections.singletonList("ikey"),"uuid");
//
////                Jedis jedis = (RedisProperties.Jedis) redisConnection.getNativeConnection();
////                Object result = jedis.eval(luaScript, Collections.singletonList(lockKey),Collections.singletonList(clientId));
//            System.out.println(result);
//            return Boolean.TRUE;
//        });
        return "OK";
    }
}