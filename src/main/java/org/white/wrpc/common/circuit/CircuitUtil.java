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

    private CircuitContext circuitContext = new CircuitContext();

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
        if (!circuitContext.canRequest(methodName)) {
            return "{\"code\":-1,\"message\":\"circuit break\"}";
        }
        String result;
        result = caller.call(serverHost, param);
        if ("exception".equals(result)) {
            circuitContext.markFail(methodName);
            return "{\"code\":-1,\"message\":\"exception request\"}";
        }
        circuitContext.markSuccess(methodName);
        return result;
    }
}
