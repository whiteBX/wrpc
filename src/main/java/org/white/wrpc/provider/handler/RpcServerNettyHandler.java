package org.white.wrpc.provider.handler;

import java.lang.reflect.Method;

import org.white.wrpc.provider.constant.ProviderConstant;
import org.white.wrpc.provider.holder.ProviderBeanHolder;

import com.alibaba.fastjson.JSON;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: RpcServerNettyHandler.java, v 0.1 2018年10月15日 11:18:00 white Exp$
 */
public class RpcServerNettyHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("服务端收到请求:" + msg);
        try {
            // 解析出 类名+方法名+请求参数类型(方法签名)
            String[] splitParam = msg.toString().split(ProviderConstant.DOLLAR_SPLIT);
            String[] beanMessage = splitParam[0].split(ProviderConstant.SHARP_SPLIT);
            // 获取注册的服务
            Object object = ProviderBeanHolder.getBean(beanMessage[0]);
            if (object == null) {
                System.out.println("服务类未注册:" + beanMessage[0]);
            }
            // 通过反射调用服务
            Class paramType = Class.forName(beanMessage[2]);
            Method method = object.getClass().getDeclaredMethod(beanMessage[1], paramType);
            Object response = method.invoke(object, JSON.parseObject(splitParam[1], paramType));
            // 请求响应
            ctx.writeAndFlush(JSON.toJSONString(response));
        } catch (Exception e) {
            System.out.println("服务异常" + e);
        }
    }
}
