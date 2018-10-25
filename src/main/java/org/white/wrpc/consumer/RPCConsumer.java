package org.white.wrpc.consumer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;

import org.white.wrpc.common.builder.SpanBuilder;
import org.white.wrpc.common.holder.SpanHolder;
import org.white.wrpc.common.model.BaseRequestBO;
import org.white.wrpc.common.model.Span;
import org.white.wrpc.common.netty.NettyClient;
import org.white.wrpc.consumer.balance.UrlHolder;
import org.white.wrpc.consumer.handler.RpcClientNettyHandler;

import com.alibaba.fastjson.JSON;

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
    private UrlHolder   urlHolder   = new UrlHolder();
    /**
     * netty客户端
     */
    private NettyClient nettyClient = new NettyClient();

    /**
     * 远程调用
     *
     * @param serverHost
     * @param param
     * @return
     */
    public String call(String serverHost, String param) {
        try {
            if (serverHost == null) {
                System.out.println("远程调用错误:当前无服务提供者");
                return "connect error";
            }
            // 连接netty,请求并接收响应
            RpcClientNettyHandler clientHandler = new RpcClientNettyHandler();
            clientHandler.setParam(param);
            nettyClient.initClient(serverHost, clientHandler);
            String result = clientHandler.process();
            System.out.println(MessageFormat.format("调用服务器:{0},请求参数:{1},响应参数:{2}", serverHost, param, result));
            return result;
        } catch (Exception e) {
            System.out.println("远程服务调用失败:" + e);
            return "error";
        }
    }

    /**
     * 获取服务
     * @param appCode
     * @return
     */
    private String getServer(String appCode) {
        // 从zookeeper获取服务地址
        String serverHost = urlHolder.getUrl(appCode);
        if (serverHost == null) {
            return null;
        }
        return serverHost;
    }

    /**
     * 获取代理类
     * @param clazz
     * @param appCode
     * @return
     */
    public <T> T getBean(final Class<T> clazz, final String appCode) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 获取服务器地址
                String serverHost = getServer(appCode);
                Span span = SpanBuilder.buildSpan(SpanHolder.get(), method.getName(), serverHost, appCode);
                //// TODO: 2018/10/25 新启线程发起rpc调用远程链路追踪服务记录追踪日志
                BaseRequestBO baseRequestBO = buildBaseBO(span, clazz.getName(), method, JSON.toJSONString(args[0]));
                return JSON.parseObject(call(serverHost, JSON.toJSONString(baseRequestBO)), method.getReturnType());
            }
        });
    }

    /**
     * 构造传输对象
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
