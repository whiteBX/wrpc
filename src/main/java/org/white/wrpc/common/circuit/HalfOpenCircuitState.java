package org.white.wrpc.common.circuit;

import javafx.util.Pair;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: HalfOpenCircuitState.java, v 0.1 2019年09月17日 14:20:00 white Exp$
 */
public class HalfOpenCircuitState extends CircuitState {
    @Override
    CircuitStatusEnum getStatus() {
        return null;
    }

    @Override
    boolean canRequest(String operation) {
        // 半开状态也不放过请求，只放过全开转换半开那一次请求
        return false;
    }

    @Override
    void markSuccess(Map<String, Pair<AtomicInteger, AtomicInteger>> counter, String operation) {
        this.context.setState(new CloseCircuitState(), operation);
        counter.put(operation, new Pair<>(new AtomicInteger(), new AtomicInteger()));
    }

    @Override
    void markFail(Map<String, Pair<AtomicInteger, AtomicInteger>> counter, String operation) {
        this.context.setState(new OpenCircuitState(System.currentTimeMillis()), operation);
    }

}
