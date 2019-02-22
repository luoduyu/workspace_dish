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
public class DLock {
    private static final Logger logger = LoggerFactory.getLogger(DLock.class);

    private String lockKey;
    private long lockExpiryInMillis;

    public DLock(StringRedisTemplate redisTemplate, String lockKey, long lockExpiryInMillis) {
        this.lockKey = lockKey;
        this.lockExpiryInMillis = lockExpiryInMillis;
    }

    public DLock(StringRedisTemplate redisTemplate, String lockKey) {
        this(redisTemplate,lockKey, Integer.MAX_VALUE);
    }

    public String getLockKey() {
        return this.lockKey;
    }

    public long getLockExpiryInMillis() {
        return lockExpiryInMillis;
    }


    private String nextUid() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }


    private boolean tryAcquire() {
       return true;
    }

    public boolean acquire(long acquireTimeoutInMillis) throws InterruptedException {
        return true;
    }

    public boolean release() throws InterruptedException {
        return release(Integer.MAX_VALUE);
    }
    public boolean release(long releaseTimeoutInMillis) throws InterruptedException {
       return true;
    }
}