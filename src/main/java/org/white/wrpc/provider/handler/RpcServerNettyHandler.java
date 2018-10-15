package org.white.wrpc.provider.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <p></p >
 *
 * @author baixiong
 * @version $Id: RpcServerNettyHandler.java, v 0.1 2018年10月15日 11:18:00 baixiong Exp$
 */
public class RpcServerNettyHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("服务端收到请求:" + msg);
        ctx.writeAndFlush("success");
    }
}
