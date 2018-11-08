package org.white.wrpc.common.netty;

import org.white.wrpc.common.constant.CommonConstant;
import org.white.wrpc.consumer.handler.RpcClientNettyHandler;
import org.white.wrpc.provider.handler.RpcServerNettyHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: NettyClient.java, v 0.1 2018年10月15日 11:03:00 white Exp$
 */
public class NettyClient {

    /**
     * 开启新的netty连接
     *
     * @param port
     */
    public void startServer(int port) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 建立TCP连接的线程池
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        // 处理消息的线程池
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(new RpcServerNettyHandler());
                    }
                });
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            // 等待通信完成
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println("netty创建服务端channel失败:" + e.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 开始一个新的客户端连接
     *
     * @param server
     */
    public void initClient(String server, final RpcClientNettyHandler clientHandler) {
        // 处理消息的线程池
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            String[] urlArray = server.split(CommonConstant.COMMOA);
            String ip = urlArray[0];
            int port = Integer.parseInt(urlArray[1]);
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(clientHandler);
                    }
                });
            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println("netty创建客户端channel失败:" + e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
