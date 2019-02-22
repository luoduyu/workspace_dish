package com.wmt.dlock.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.UUID;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  锁
 *
 * @author adu Create on 2019-02-15 16:01
 * @version 1.0
 */
public class DistributedLock{
    private static final Logger logger = LoggerFactory.getLogger(DistributedLock.class);



    /**
     * lock Key
     */
    private String lockKey;

    /**
     * 锁的过期时长,单位纳秒
     */
    private long lockExpiryInMillis;


    /**
     * 构造方法
     *
     * @param lockKey            锁的Key
     * @param lockExpiryInMillis 锁的过期时长,单位毫秒
     */
    public DistributedLock(StringRedisTemplate redisTemplate,String lockKey, long lockExpiryInMillis) {
        this.lockKey = lockKey;
        this.lockExpiryInMillis = lockExpiryInMillis;
    }

    /**
     * 构造方法
     * <p>
     * 使用锁默认的过期时长Integer.MAX_VALUE,即锁永远不会过期

     * @param lockKey   锁的Key
     */
    public DistributedLock(StringRedisTemplate redisTemplate,String lockKey) {
        this(redisTemplate,lockKey, Integer.MAX_VALUE);
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
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 请求锁,不会阻塞,直接返回
     *
     * @return 成功获取锁返回true, 否则返回false
     */
    private boolean tryAcquire() {
       return true;
    }

    /**
     * 超时请求锁,会阻塞
     *
     * 采用"自旋获取锁"的方式,直至获取锁成功或者请求锁超时
     *
     * @param acquireTimeoutInMillis 锁的请求超时时长(毫秒)
     * @return
     */
    public boolean acquire(long acquireTimeoutInMillis) throws InterruptedException {


        return true;
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
       return true;
    }

    private static int string2Int(String param){
        try {
            return Integer.parseInt(param.trim());
        } catch (NumberFormatException e) {
            logger.error("param="+param+",e="+e.getMessage(),e);
            return 0;
        }
    }
}