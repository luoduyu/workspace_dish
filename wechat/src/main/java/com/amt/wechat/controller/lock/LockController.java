package com.amt.wechat.controller.lock;

import com.wmt.dlock.lock.DistributedLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    private static final DistributedLock lock = new DistributedLock("seckillV2_" + UUID.randomUUID().toString());

    static {
        longAdder.add(stock);
    }

    private @Resource RedisTemplate redisTemplate;

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
                if (!releaseResult) {
                    logger.error("释放锁失败！");
                }
            }
        }

        return "OK";
    }

    private void doSomeThing() {
        try {
            long ret = redisTemplate.opsForValue().increment("already_bought");
            logger.info("doSomeThing={}"+ret);
        } catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }
}
