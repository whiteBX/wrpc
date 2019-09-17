package org.white.wrpc.common.circuit;

import javafx.util.Pair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: CircuitContext.java, v 0.1 2019年09月17日 14:32:00 white Exp$
 */
public class CircuitContext {


    // 达到默认请求基数才判断开启熔断
    protected static final int DEFAULT_FAIL_COUNT = 5;
    // 默认失败比例值开启熔断
    protected static final double DEFAULT_FAIL_RATE = 0.8D;
    // 半开转换为全开时间
    protected static final long DEFAULT_HALF_OPEN_TRANSFER_TIME = 10000;
    // 计数 pair左边成功,右边失败
    protected volatile Map<String, Pair<AtomicInteger, AtomicInteger>> counter = new ConcurrentHashMap<>();
    // 每个operation对应的状态
    private volatile Map<String, CircuitState> state = new ConcurrentHashMap<>();

    /**
     * 判断该次请求是否可以通过
     * @param operation
     * @return
     */
    public boolean canRequest(String operation) {
        return getState(operation).canRequest(operation);
    }

    /**
     * 标记成功
     * 1.半开状态,成功一次转换为关闭状态
     * 2.其他情况增加成功记录次数
     *
     * @param operation
     */
    public void markSuccess(String operation) {
        Pair<AtomicInteger, AtomicInteger> pair = counter.get(operation);
        if (pair == null) {
            counter.put(operation, new Pair<>(new AtomicInteger(), new AtomicInteger()));
        }
        this.getState(operation).markSuccess(counter, operation);
    }

    /**
     * 标记失败
     * 1.半开状态,失败一次回退到打开状态
     * 2.其他状态判断错误比例决定是否打开熔断
     *
     * @param operation
     */
    public void markFail(String operation) {
        Pair<AtomicInteger, AtomicInteger> pair = counter.get(operation);
        if (pair == null) {
            counter.put(operation, new Pair<>(new AtomicInteger(), new AtomicInteger()));
        }
        this.getState(operation).markFail(counter, operation);
    }

    public CircuitState getState(String operation) {
        if (this.state.get(operation) == null) {
            CircuitState state = new CloseCircuitState();
            state.setContext(this);
            this.state.put(operation, state);
        }
        return this.state.get(operation);
    }

    public void transferState(CircuitState state, String operation) {
        state.setContext(this);
        this.state.put(operation, state);
    }
}
