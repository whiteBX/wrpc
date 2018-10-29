package org.white.wrpc.common.holder;

import org.white.wrpc.common.model.Span;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: SpanHolder.java, v 0.1 2018年10月25日 16:44:00 white Exp$
 */
public class SpanHolder {
    /**
     * 存放线程span
     */
    private static ThreadLocal<Span> spanThreadLocal = new ThreadLocal<Span>();

    /**
     * 在provider接收到消息时调用,存入span
     * @param span
     */
    public static void put(Span span) {
        spanThreadLocal.set(span);
    }

    /**
     * 在调用远程服务时取值
     * @return
     */
    public static Span get() {
        return spanThreadLocal.get();
    }

    /**
     * 清除
     */
    public static void clear() {
        spanThreadLocal.remove();
    }
}
