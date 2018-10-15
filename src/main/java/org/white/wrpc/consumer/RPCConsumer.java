package org.white.wrpc.consumer;

import org.white.wrpc.common.netty.NettyClient;
import org.white.wrpc.consumer.balance.UrlHolder;
import org.white.wrpc.consumer.handler.RpcClientNettyHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.MessageFormat;

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
     * netty客户端
     */
    private NettyClient nettyClient = new NettyClient();

    /**
     * 远程调用
     *
     * @param appCode
     * @param param
     * @return
     */
    public String call(String appCode, String param) {
        try {
            // 从zookeeper获取服务地址
            String serverIp = urlHolder.getUrl(appCode);
            if (serverIp == null) {
                System.out.println("远程调用错误:当前无服务提供者");
                return "connect error";
            }
            // 连接netty,请求并接收响应
            RpcClientNettyHandler clientHandler = new RpcClientNettyHandler();
            clientHandler.setParam(param);
            nettyClient.initClient(serverIp, clientHandler);
            String result = clientHandler.process();
            System.out.println(MessageFormat.format("调用服务器:{0},请求参数:{1},响应参数:{2}", serverIp, param, result));
            return result;
        } catch (Exception e) {
            System.out.println("远程服务调用失败:" + e);
            return "error";
        }
    }

    public Object proxy(Class<?> clazz, final String appCode) {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            public String invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String serverIp = urlHolder.getUrl(appCode);
                RpcClientNettyHandler clientHandler = new RpcClientNettyHandler();
                clientHandler.setParam(args[0].toString());
                nettyClient.initClient(serverIp, clientHandler);
                return clientHandler.process();
            }
        });
    }
}
