package org.white.wrpc.common.circuit;

import javafx.util.Pair;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: CloseCircuitState.java, v 0.1 2019年09月17日 14:20:00 white Exp$
 */
public class CloseCircuitState extends CircuitState {

    @Override
    CircuitStatusEnum getStatus() {
        return CircuitStatusEnum.CLOSE;
    }

    @Override
    boolean canRequest(String operation) {
        return true;
    }

    @Override
    void markSuccess(Map<String, Pair<AtomicInteger, AtomicInteger>> counter, String operation) {
        counter.get(operation).getKey().incrementAndGet();
    }

    @Override
    void markFail(Map<String, Pair<AtomicInteger, AtomicInteger>> counter, String operation) {
        Pair<AtomicInteger, AtomicInteger> pair = counter.get(operation);
        int failCount = pair.getValue().incrementAndGet();
        int successCount = pair.getKey().get();
        int totalCount = failCount + successCount;
        double failRate = (double) failCount / (double) totalCount;
        if (totalCount >= this.context.DEFAULT_FAIL_COUNT && failRate > this.context.DEFAULT_FAIL_RATE) {
            this.context.transferState(new OpenCircuitState(System.currentTimeMillis()), operation);
        }
    }
}
