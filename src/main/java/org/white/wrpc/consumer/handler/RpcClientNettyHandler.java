package org.white.wrpc.consumer.handler;

import java.util.concurrent.CountDownLatch;

import org.white.wrpc.consumer.processor.MethodProcessor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <p></p >
 *
 * @author baixiong
 * @version $Id: RpcServerNettyHandler.java, v 0.1 2018年10月15日 11:18:00 baixiong Exp$
 */
public class RpcClientNettyHandler extends ChannelInboundHandlerAdapter implements MethodProcessor {

    private ChannelHandlerContext context;
    private CountDownLatch        countDownLatch = new CountDownLatch(1);
    /**
     * 入参
     */
    private String                param;
    /**
     * 响应
     */
    private String                response;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        response = msg.toString();
        countDownLatch.countDown();
    }

    public String process() throws InterruptedException {
        context.writeAndFlush(param);
        countDownLatch.await();
        return response;
    }

    public void setParam(String param) {
        this.param = param;
    }

}
