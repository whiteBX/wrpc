package org.white.wrpc.consumer.balance;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p> </p >
 *
 * @author white
 * @version $Id: RoundRobinBalanceProcessor.java, v 0.1 2018年10月24日 上午22:26:00 white Exp$
 */
public class RoundRobinBalanceProcessor implements BalanceProcessor {

    private ConcurrentMap<String, AtomicInteger> counterMap = new ConcurrentHashMap<String, AtomicInteger>();


    public String process(String appCode, List<String> urlList) {
        AtomicInteger counter = counterMap.get(appCode);
        if (counter == null) {
            counterMap.putIfAbsent(appCode, new AtomicInteger());
        }
        counter = counterMap.get(appCode);
        int current = getAndIncrement(counter);

        return urlList.get(current % urlList.size());
    }

    /**
     * 通过CAS获取不超过最大数的顺序int
     *
     * @param ai
     * @return
     */
    private Integer getAndIncrement(AtomicInteger ai) {
        while (true) {
            int current = ai.get();
            int next = current >= Integer.MAX_VALUE ? 0 : current + 1;
            if (ai.compareAndSet(current, next)) {
                return current;
            }
        }
    }
}
