package org.white.wrpc.provider;

import org.apache.zookeeper.ZooKeeper;
import org.white.wrpc.common.constant.CommonConstant;
import org.white.wrpc.common.netty.NettyClient;
import org.white.wrpc.common.zk.ZKClient;
import org.white.wrpc.provider.constant.ProviderConstant;
import org.white.wrpc.provider.holder.ProviderBeanHolder;

/**
 * <p> 1.创建netty监听,等待客户端连接 2.连接zk,创建临时节点,记录服务器连接信息 </p >
 *
 * @author white
 * @version $Id: RPCProvider.java, v 0.1 2018年10月15日 17:42:00 white Exp$
 */
public class RPCProvider {
    /**
     * netty客户端
     */
    private static NettyClient nettyClient = new NettyClient();
    /**
     * zookeeper客户端
     */
    private static ZKClient    zkClient    = new ZKClient();

    public void registry(String server, int port) {
        // 开启netty监听客户端连接
        nettyClient.startServer(port);
        // 创建zk连接并创建临时节点
        ZooKeeper zooKeeper = zkClient.newConnection(ProviderConstant.ZK_CONNECTION_STRING,
            ProviderConstant.ZK_SESSION_TIME_OUT);
        String serverIp = server + CommonConstant.COMMOA + port;
        zkClient.createEphemeralNode(zooKeeper, ProviderConstant.APP_CODE, serverIp.getBytes());
    }

    /**
     * 注册服务提供者
     * @param clazz
     * @param obj
     */
    public void provide(Class<?> clazz, Object obj) {
        ProviderBeanHolder.regist(clazz.getName(), obj);
    }
}
