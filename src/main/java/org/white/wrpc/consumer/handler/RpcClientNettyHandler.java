package org.white.wrpc.consumer.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.white.wrpc.consumer.processor.MethodProcessor;

import java.util.concurrent.CountDownLatch;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: RpcServerNettyHandler.java, v 0.1 2018年10月15日 11:18:00 white Exp$
 */
public class RpcClientNettyHandler extends ChannelInboundHandlerAdapter implements MethodProcessor {

    private ChannelHandlerContext context;
    private CountDownLatch contextCountDownLatch = new CountDownLatch(1);
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    /**
     * 入参
     */
    private String param;
    /**
     * 响应
     */
    private String response;

    /**
     * 连上时触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
        contextCountDownLatch.countDown();
    }

    /**
     * 获取服务器返回信息
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        response = msg.toString();
        countDownLatch.countDown();
    }

    /**
     * 远程调用并返回结果
     *
     * @return
     * @throws InterruptedException
     */
    public String process() throws InterruptedException {
        contextCountDownLatch.await();
        context.writeAndFlush(param);
        countDownLatch.await();
        return response;
    }

    public void setParam(String param) {
        this.param = param;
    }

}
