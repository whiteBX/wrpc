package org.white.wrpc.consumer.constant;

/**
 * <p>配置文件内容,此处直接通过常量代替</p >
 *
 * @author white
 * @version $Id: ConsumerProperties.java, v 0.1 2018年10月15日 11:41:00 white Exp$
 */
public interface ConsumerProperties {
    /**
     * zookeeper连接串
     */
    String ZK_CONNECTION_STRING       = "127.0.0.1:2181";
    /**
     * zookeeper超时时间
     */
    int    ZK_SESSION_TIME_OUT        = 5000000;
    /**
     * 服务唯一编码
     */
    String APP_CODE                   = "100000";
    /**
     * 调用的服务编码
     */
    String SERVER_APP_CODE            = "100000";


    /**
     * 服务限流默认值
     */
    int    RATE_LIMITER_DEFAULT_VALUE = 1;
}
