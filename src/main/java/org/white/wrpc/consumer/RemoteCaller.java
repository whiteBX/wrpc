package org.white.wrpc.consumer;

import org.white.wrpc.common.Caller;
import org.white.wrpc.common.netty.NettyClient;
import org.white.wrpc.consumer.handler.RpcClientNettyHandler;

import java.text.MessageFormat;

/**
 * <p> </p >
 *
 * @author white
 * @version $Id: RemoteCaller.java, v 0.1 2019年06月22日 上午16:42:00 white Exp$
 */
public class RemoteCaller implements Caller {
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
    @Override
    public String call(String serverHost, String param) {
        try {
            if (serverHost == null) {
                System.out.println("远程调用错误:当前无服务提供者");
                return "{\"code\":404,\"message\":\"no provider\"}";
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
}
