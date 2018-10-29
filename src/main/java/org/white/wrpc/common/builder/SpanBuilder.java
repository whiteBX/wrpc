package org.white.wrpc.common.builder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import org.white.wrpc.common.model.Span;
import org.white.wrpc.common.utils.ShortUUIDUtils;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: SpanBuilder.java, v 0.1 2018年10月25日 16:33:00 white Exp$
 */
public class SpanBuilder {

    /**
     * 构造span
     * @param parentSpan
     * @return
     * @throws UnknownHostException
     */
    public static Span buildNewSpan(Span parentSpan, String operationName, String serverIp, String appCode) throws UnknownHostException {
        Span span = new Span();
        span.setLocalIp(InetAddress.getLocalHost().getHostAddress());
        if (parentSpan == null) {
            span.setTraceId(ShortUUIDUtils.nextId());
            span.setParentSpanId("0");
        } else {
            span.setTraceId(parentSpan.getTraceId());
            span.setParentSpanId(parentSpan.getSpanId());
        }
        span.setTimestamp(System.currentTimeMillis());
        span.setOperationName(operationName);
        span.setRemoteIp(serverIp);
        span.setAppCode(appCode);
        span.setSpanId(ShortUUIDUtils.nextId());
        return span;
    }

    /**
     * 构建新的appCpde的Span
     * @param span
     * @param appCode
     * @return
     */
    public static Span rebuildSpan(Span span, String appCode) {
        Span newSpan = copy(span);
        newSpan.setAppCode(appCode);
        return newSpan;
    }

    /**
     * 拷贝
     * @param source
     * @return
     */
    public static Span copy(Span source) {
        if (source == null) {
            return null;
        }
        Span span = new Span();
        span.setTraceId(source.getTraceId());
        span.setOperationName(source.getOperationName());
        span.setSpanId(source.getSpanId());
        span.setParentSpanId(source.getParentSpanId());
        span.setAppCode(source.getAppCode());
        span.setLocalIp(source.getLocalIp());
        span.setRemoteIp(source.getRemoteIp());
        span.setTimestamp(source.getTimestamp());
        return span;
    }
}
