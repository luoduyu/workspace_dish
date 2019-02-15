package com.wmt.dlock.lock;

import com.alibaba.fastjson.JSON;
import com.wmt.dlock.config.MyRedisScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  分布式锁
 *
 * @author adu Create on 2019-02-15 16:01
 * @version 1.0
 */
public class DistributedLock{
    private static final Logger logger = LoggerFactory.getLogger(DistributedLock.class);


    /**
     * 重试屏障，单位毫秒
     */
    private static final long RETRY_BARRIER = 600;

    /**
     * 下一次重试等待，单位毫秒
     */
    private static final long INTERVAL_TIMES = 200;


    /**
     * lock Key
     */
    private final String lockKey;

    /**
     * 锁的过期时长,单位纳秒
     */
    private final long lockExpiryInMillis;

    /**
     * 存放当前线程锁
     */
    private final ThreadLocal<Lock> lockThreadLocal = new ThreadLocal<Lock>();

    private @Resource
    MyRedisScript myRedisScript;
    private @Autowired RedisTemplate<String, Serializable> redisTemplate;


    /**
     * 构造方法
     *
     * @param lockKey            锁的Key
     * @param lockExpiryInMillis 锁的过期时长,单位毫秒
     */
    public DistributedLock(String lockKey, long lockExpiryInMillis) {

        this.lockKey = lockKey;
        this.lockExpiryInMillis = lockExpiryInMillis;
    }

    /**
     * 构造方法
     * <p>
     * 使用锁默认的过期时长Integer.MAX_VALUE,即锁永远不会过期

     * @param lockKey   锁的Key
     */
    public DistributedLock(String lockKey) {
        this(lockKey, Integer.MAX_VALUE);
    }

    /**
     * 获取锁在redis中的Key标记
     *
     * @return locks key
     */
    public String getLockKey() {
        return this.lockKey;
    }

    /**
     * 锁的过期时长
     *
     * @return
     */
    public long getLockExpiryInMillis() {
        return lockExpiryInMillis;
    }

    /**
     * can override
     * @return
     */
    private String nextUid() {
        // 可以考虑雪花算法..
        return UUID.randomUUID().toString();
    }

    /**
     * 请求分布式锁,不会阻塞,直接返回
     *
     * @return 成功获取锁返回true, 否则返回false
     */
    private boolean tryAcquire() {
        final Lock nLock = new Lock(nextUid());
        if(redisTemplate.opsForValue().setIfAbsent(lockKey,nLock.toString(),lockExpiryInMillis,TimeUnit.MILLISECONDS)){
            lockThreadLocal.set(nLock);
            return true;
        }
        return false;
    }

    /**
     * 超时请求分布式锁,会阻塞
     *
     * 采用"自旋获取锁"的方式,直至获取锁成功或者请求锁超时
     *
     * @param acquireTimeoutInMillis 锁的请求超时时长
     * @return
     */
    public boolean acquire(long acquireTimeoutInMillis) throws InterruptedException {
        //long acquireTime = System.currentTimeMillis();

        // 锁的请求到期时间
        long expiryTime = System.currentTimeMillis() + acquireTimeoutInMillis;
        while (expiryTime >= System.currentTimeMillis()) {
            boolean result = tryAcquire();
            if (result) { // 获取锁成功直接返回,否则循环重试
                return true;
            }

            Thread.sleep(INTERVAL_TIMES);
        }
        return false;
    }

    /**
     * 释放锁(会阻塞指定时间)
     *
     * @return
     */
    public boolean release() throws InterruptedException {
        return release(Integer.MAX_VALUE);
    }

    /**
     * 释放锁(会阻塞指定时间)
     *
     * @param releaseTimeoutInMillis
     * @return
     */
    public boolean release(long releaseTimeoutInMillis) throws InterruptedException {
        Lock cLock = lockThreadLocal.get();
        if (cLock == null) {
            logger.info("lock is null!");
            return true;
        }

        //long releaseTime = System.currentTimeMillis();

        // 执行释放锁动作的最后期限
        long expiryTime = System.currentTimeMillis() + releaseTimeoutInMillis;
        while (expiryTime >= System.currentTimeMillis()) {

            String strResult = redisTemplate.execute(myRedisScript, RedisSerializer.string(),RedisSerializer.string(),Collections.singletonList(this.lockKey),Collections.singletonList(cLock.toString()));

            int ret = string2Int(strResult);
            if (ret == 1) {
                lockThreadLocal.remove();
                return true;
            }
            Thread.sleep(INTERVAL_TIMES);
        }

        return false;
    }
    private static int string2Int(String param){
        try {
            return Integer.parseInt(param.trim());
        } catch (NumberFormatException e) {
            logger.error("param="+param+",e="+e.getMessage(),e);
            return 0;
        }
    }

    /**
     * 锁
     */
    protected static class Lock {
        /**
         * lock 唯一标识
         */
        private String uid;

        Lock(String uid) {
            this.uid = uid;
        }

        public String getUid() {
            return uid;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this, false);
        }
    }
}