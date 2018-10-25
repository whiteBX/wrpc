package org.white.wrpc.common.builder;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

import org.white.wrpc.common.model.Span;

/**
 * <p></p >
 *
 * @author baixiong
 * @version $Id: SpanBuilder.java, v 0.1 2018年10月25日 16:33:00 baixiong Exp$
 */
public class SpanBuilder {

    /**
     * 构造span
     * @param parentSpan
     * @return
     * @throws UnknownHostException
     */
    public static Span buildSpan(Span parentSpan, String operationName, String serverIp, String appCode) throws UnknownHostException {
        Span span = new Span();
        span.setLocalIp(InetAddress.getLocalHost().getHostAddress());
        if (parentSpan == null) {
            span.setTraceId(UUID.randomUUID().toString().replaceAll("-", ""));
            span.setParentSpanId("0");
        } else {
            span.setTraceId(parentSpan.getTraceId());
            span.setParentSpanId(parentSpan.getSpanId());
        }
        span.setTimestamp(System.currentTimeMillis());
        span.setOperationName(operationName);
        span.setRemoteIp(serverIp);
        span.setAppCode(appCode);
        return span;
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
