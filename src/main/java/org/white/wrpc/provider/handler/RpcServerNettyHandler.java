package org.white.wrpc.provider.handler;

import java.lang.reflect.Method;

import org.white.wrpc.common.holder.SpanHolder;
import org.white.wrpc.common.model.BaseRequestBO;
import org.white.wrpc.common.model.BaseResponseBO;
import org.white.wrpc.common.model.Span;
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
            ctx.writeAndFlush(buildResponse(baseRequestBO.getSpan(), JSON.toJSONString(response)));
        } catch (Exception e) {
            System.out.println("服务异常" + e);
        }
    }

    /**
     * 构造响应
     * @param span
     * @param response
     * @return
     */
    private BaseResponseBO buildResponse(Span span, String response) {
        BaseResponseBO baseResponseBO = new BaseResponseBO();
        baseResponseBO.setSpan(span);
        baseResponseBO.setResponse(response);
        return baseResponseBO;
    }
}
