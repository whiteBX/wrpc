package org.white.wrpc.consumer.constant;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: ConsumerConstant.java, v 0.1 2018年10月15日 11:41:00 white Exp$
 */
public interface ConsumerConstant {
    /**
     * 注册中心根节点
     */
    String ZK_REGISTORY_ROOT_PATH = "/registry";
    /**
     * zookeeper连接串
     */
    String ZK_CONNECTION_STRING   = "";
    /**
     * zookeeper超时时间
     */
    int    ZK_SESSION_TIME_OUT    = 5000;
    /**
     * 服务唯一编码
     */
    String APP_CODE               = "100000";
}
