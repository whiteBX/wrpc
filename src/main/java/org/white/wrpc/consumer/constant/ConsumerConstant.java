package org.white.wrpc.consumer.constant;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: ConsumerConstant.java, v 0.1 2018年10月15日 11:41:00 white Exp$
 */
public interface ConsumerConstant {
    /**
     * zookeeper连接串
     */
    String ZK_CONNECTION_STRING   = "127.0.0.1:2181";
    /**
     * zookeeper超时时间
     */
    int    ZK_SESSION_TIME_OUT    = 5000000;
    /**
     * 服务唯一编码
     */
    String APP_CODE               = "100000";
}
