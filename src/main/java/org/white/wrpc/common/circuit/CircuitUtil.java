package org.white.wrpc.common.circuit;

import javafx.util.Pair;
import org.white.wrpc.common.Caller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: CircuitAspect.java, v 0.1 2019年05月14日 11:39:00 white Exp$
 */
public class CircuitUtil {

    // 达到默认请求基数才判断开启熔断
    private static final int DEFAULT_FAIL_COUNT = 5;
    // 半开转换为全开时间
    private static final long DEFAULT_HALF_OPEN_TRANSFER_TIME = 10000;
    // 默认失败比例值开启熔断
    private static final double DEFAULT_FAIL_RATE = 0.8D;
    // 计数 pair左边成功,右边失败
    private Map<String, Pair<AtomicInteger, AtomicInteger>> counter = new ConcurrentHashMap<>();

    private volatile CircuitStatusEnum status = CircuitStatusEnum.CLOSE;

    private volatile long timestamp;
    private final Semaphore semaphore = new Semaphore(1);

    /**
     * 简易熔断流程
     * 1:判断是否打开熔断,打开则直接返回指定信息
     * 2:执行逻辑,成功失败都进行标记 markSuccess markFail
     *
     * @param caller
     * @return
     * @throws Throwable
     */
    public String doCircuit(String methodName, Caller caller, String serverHost, String param) throws Throwable {
        if (isOpen()) {
            return "{\"code\":-1,\"message\":\"circuit break\"}";
        }
        String result;
        result = caller.call(serverHost, param);
        if ("exception".equals(result)) {
            markFail(methodName);
            return "{\"code\":-1,\"message\":\"exception request\"}";
        }
        markSuccess(methodName);
        return result;
    }

    /**
     * 判断熔断是否打开 全开状态是判断是否转为半开并放过一个请求
     *
     * @return
     */
    private boolean isOpen() {
        boolean openFlag = true;
        // 关闭
        if (status.equals(CircuitStatusEnum.CLOSE)) {
            openFlag = false;
        }
        // 全开
        if (status.equals(CircuitStatusEnum.OPEN)) {
            // 未到半开时间,返回打开
            if (System.currentTimeMillis() - timestamp < DEFAULT_HALF_OPEN_TRANSFER_TIME) {
                return openFlag;
            }
            // 已到半开时间,改为半开状态,通过一个请求
            if (semaphore.tryAcquire()) {
                status = CircuitStatusEnum.HALF_OPEN;
                timestamp = System.currentTimeMillis();
                openFlag = false;
                semaphore.release();
            }
        }
        return openFlag;
    }

    /**
     * 标记成功
     * 1.半开状态,成功一次转换为关闭状态
     * 2.其他情况增加成功记录次数
     *
     * @param operation
     */
    private void markSuccess(String operation) {
        Pair<AtomicInteger, AtomicInteger> pair = counter.get(operation);
        if (pair == null) {
            counter.put(operation, new Pair<>(new AtomicInteger(), new AtomicInteger()));
        }
        // 半开状态,成功一次转换为关闭状态
        if (status == CircuitStatusEnum.HALF_OPEN) {
            status = CircuitStatusEnum.CLOSE;
            counter.put(operation, new Pair<>(new AtomicInteger(), new AtomicInteger()));
        } else {
            counter.get(operation).getKey().incrementAndGet();
        }
    }

    /**
     * 标记失败
     * 1.半开状态,失败一次回退到打开状态
     * 2.其他状态判断错误比例决定是否打开熔断
     *
     * @param operation
     */
    private void markFail(String operation) {
        // 半开状态失败变更为全开,否则计数判断
        if (status == CircuitStatusEnum.HALF_OPEN) {
            status = CircuitStatusEnum.OPEN;
            timestamp = System.currentTimeMillis();
        } else {
            Pair<AtomicInteger, AtomicInteger> pair = counter.get(operation);
            if (pair == null) {
                counter.put(operation, new Pair<>(new AtomicInteger(), new AtomicInteger()));
                pair = counter.get(operation);
            }
            int failCount = pair.getValue().incrementAndGet();
            int successCount = pair.getKey().get();
            int totalCount = failCount + successCount;
            double failRate = (double) failCount / (double) totalCount;
            if (totalCount >= DEFAULT_FAIL_COUNT && failRate > DEFAULT_FAIL_RATE) {
                status = CircuitStatusEnum.OPEN;
                timestamp = System.currentTimeMillis();
            }
        }
    }
}
