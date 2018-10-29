package org.white.wrpc.consumer.limiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.RateLimiter;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: ConsumerLimiter.java, v 0.1 2018年10月29日 16:16:00 white Exp$
 */
public class ConsumerLimiter {

    /**
     * 存放限流器
     */
    private ConcurrentMap<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<String, RateLimiter>();

    /**
     * 限流
     * @param appCode
     * @return
     */
    public boolean limit(String appCode) {
        RateLimiter rateLimiter = rateLimiterMap.get(appCode);
        if (rateLimiter == null) {
            rateLimiterMap.putIfAbsent(appCode, RateLimiter.create(1));
        }
        rateLimiter = rateLimiterMap.get(appCode);
        // 默认等待1S
        return rateLimiter.tryAcquire(1, TimeUnit.SECONDS);
    }
}
