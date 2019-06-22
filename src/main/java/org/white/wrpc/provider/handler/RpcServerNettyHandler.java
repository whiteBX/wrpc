package org.white.wrpc.provider.handler;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.white.wrpc.common.builder.SpanBuilder;
import org.white.wrpc.common.holder.SpanHolder;
import org.white.wrpc.common.model.BaseRequestBO;
import org.white.wrpc.common.model.Span;
import org.white.wrpc.provider.constant.ProviderProperties;
import org.white.wrpc.provider.holder.ProviderBeanHolder;

import java.lang.reflect.Method;

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
            BaseRequestBO baseRequestBO = JSON.parseObject(msg.toString(), BaseRequestBO.class);
            // 放入span
            SpanHolder.put(baseRequestBO.getSpan());
            // 获取注册的服务
            Object object = ProviderBeanHolder.getBean(baseRequestBO.getClazzName());
            if (object == null) {
                System.out.println("服务类未注册:" + baseRequestBO.getClazzName());
            }
            // 通过反射调用服务
            Class paramType = Class.forName(baseRequestBO.getParamTypeName());
            Method method = object.getClass().getDeclaredMethod(baseRequestBO.getMethodName(), paramType);
            Object response = method.invoke(object, JSON.parseObject(baseRequestBO.getData(), paramType));
            // 请求响应
            ctx.writeAndFlush(JSON.toJSONString(response)).addListener(ChannelFutureListener.CLOSE);
            Span span = SpanBuilder.rebuildSpan(baseRequestBO.getSpan(), ProviderProperties.APP_CODE);
            //// TODO: 2018/10/25 新启线程发起rpc调用远程链路追踪服务记录追踪日志 此处打日志代替
            System.out.println("链路追踪，远程服务响应：" + JSON.toJSONString(span));
        } catch (Exception e) {
            System.out.println("服务异常" + e);
            ctx.writeAndFlush("exception").addListener(ChannelFutureListener.CLOSE);
        }
    }
}
