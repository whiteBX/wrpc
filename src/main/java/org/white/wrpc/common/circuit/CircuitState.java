package org.white.wrpc.common.circuit;

import javafx.util.Pair;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: CircuitState.java, v 0.1 2019年09月17日 14:19:00 white Exp$
 */
public abstract class CircuitState {

    protected CircuitContext context;

    public void setContext(CircuitContext context) {
        this.context = context;
    }

    /**
     * 获取当前状态
     */
    abstract CircuitStatusEnum getStatus();

    /**
     * 能否通过请求
     * @return
     */
    abstract boolean canRequest(String operation);

    /**
     * 标记成功
     */
    abstract void markSuccess(Map<String, Pair<AtomicInteger, AtomicInteger>> counter, String operation);

    /**
     * 标记失败
     */
    abstract void markFail(Map<String, Pair<AtomicInteger, AtomicInteger>> counter, String operation);
}
