package org.white.wrpc.common.circuit;

import javafx.util.Pair;

import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: OpenCircuitState.java, v 0.1 2019年09月17日 14:19:00 white Exp$
 */
public class OpenCircuitState extends CircuitState{

    private volatile long timestamp;
    private final Semaphore semaphore = new Semaphore(1);

    public OpenCircuitState(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    CircuitStatusEnum getStatus() {
        return CircuitStatusEnum.OPEN;
    }

    @Override
    boolean canRequest(String operation) {
        // 未到半开时间,返回打开
        if (System.currentTimeMillis() - timestamp < this.context.DEFAULT_HALF_OPEN_TRANSFER_TIME) {
            return false;
        }
        // 已到半开时间,改为半开状态,通过一个请求
        if (semaphore.tryAcquire()) {
            this.context.transferState(new HalfOpenCircuitState(), operation);
            semaphore.release();
            return true;
        }

        return false;
    }

    @Override
    void markSuccess(Map<String, Pair<AtomicInteger, AtomicInteger>> counter, String operation) {
        // do nothing
    }

    @Override
    void markFail(Map<String, Pair<AtomicInteger, AtomicInteger>> counter, String operation) {
        // do nothing
    }
}
