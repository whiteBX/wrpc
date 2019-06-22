package org.white.wrpc.consumer;

import com.alibaba.fastjson.JSON;
import org.white.wrpc.common.builder.SpanBuilder;
import org.white.wrpc.common.circuit.CircuitUtil;
import org.white.wrpc.common.holder.SpanHolder;
import org.white.wrpc.common.model.BaseRequestBO;
import org.white.wrpc.common.model.Span;
import org.white.wrpc.consumer.balance.UrlHolder;
import org.white.wrpc.consumer.limiter.ConsumerLimiter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: RPCConsumer.java, v 0.1 2018年10月15日 17:46:00 white Exp$
 */
public class RPCConsumer {

    /**
     * url处理器
     */
    private UrlHolder urlHolder = new UrlHolder();
    /**
     * 远程调用
     */
    private RemoteCaller remoteCaller = new RemoteCaller();
    /**
     * 限流器
     */
    private ConsumerLimiter consumerLimiter = new ConsumerLimiter();
    /**
     * 熔断器
     */
    private CircuitUtil circuitUtil = new CircuitUtil();


    /**
     * 获取服务
     *
     * @param appCode
     * @return
     */
    private String getServer(String appCode) {
        // 限流
        if (!consumerLimiter.limit(appCode)) {
            System.out.println("请求被限流");
            return null;
        }
        // 从zookeeper获取服务地址
        String serverHost = urlHolder.getUrl(appCode);
        if (serverHost == null) {
            return null;
        }
        return serverHost;
    }

    /**
     * 获取代理类
     *
     * @param clazz
     * @param appCode
     * @return
     */
    public <T> T getBean(final Class<T> clazz, final String appCode) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 获取服务器地址
                String serverHost = getServer(appCode);
                Span span = SpanBuilder.buildNewSpan(SpanHolder.get(), method.getName(), serverHost, appCode);
                //// TODO: 2018/10/25 新启线程发起rpc调用远程链路追踪服务记录追踪日志 此处打日志代替
                System.out.println("链路追踪，调用远程服务：" + JSON.toJSONString(span));
                BaseRequestBO baseRequestBO = buildBaseBO(span, clazz.getName(), method, JSON.toJSONString(args[0]));
                String result = circuitUtil.doCircuit(method.getName(), remoteCaller, serverHost, JSON.toJSONString(baseRequestBO));
                return JSON.parseObject(result, method.getReturnType());
            }
        });
    }

    /**
     * 构造传输对象
     *
     * @param span
     * @param clazzName
     * @param method
     * @param data
     * @return
     */
    private BaseRequestBO buildBaseBO(Span span, String clazzName, Method method, String data) {
        BaseRequestBO baseRequestBO = new BaseRequestBO();
        baseRequestBO.setSpan(span);
        baseRequestBO.setClazzName(clazzName);
        baseRequestBO.setMethodName(method.getName());
        baseRequestBO.setParamTypeName(method.getParameterTypes()[0].getName());
        baseRequestBO.setData(data);
        return baseRequestBO;
    }
}
