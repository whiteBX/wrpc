package org.white.wrpc.common.netty;

import org.white.wrpc.common.constant.CommonConstant;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.white.wrpc.consumer.handler.RpcClientNettyHandler;
import org.white.wrpc.provider.handler.RpcServerNettyHandler;

/**
 * <p></p >
 *
 * @author baixiong
 * @version $Id: NettyClient.java, v 0.1 2018年10月15日 11:03:00 baixiong Exp$
 */
public class NettyClient {

    /**
     * 开启新的netty连接
     * @param port
     */
    public void startServer(int port) {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(new NioEventLoopGroup()).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new RpcServerNettyHandler());
                    }
                });
            bootstrap.bind(port).sync();
        } catch (InterruptedException e) {
            System.out.println("netty创建服务端channel失败:" + e.getMessage());
        }
    }

    /**
     * 开始一个新的客户端连接
     * @param server
     */
    public void initClient(String server, final RpcClientNettyHandler clientHandler) {
        try {
            String[] urlArray = server.split(CommonConstant.COMMOA);
            String ip = urlArray[0];
            int port = Integer.parseInt(urlArray[1]);
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(new NioEventLoopGroup()).channel(NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true).childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel socketChannel) {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(clientHandler);
                    }
                });
            bootstrap.bind(ip, port).sync();
        } catch (InterruptedException e) {
            System.out.println("netty创建客户端channel失败:" + e.getMessage());
        }
    }
}
