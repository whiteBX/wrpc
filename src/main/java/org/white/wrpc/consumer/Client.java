package org.white.wrpc.consumer;

import org.white.wrpc.consumer.constant.ConsumerConstant;

/**
 * <p> 1.从zk中读取appCode下的所有子节点数据,获取urlList 2.创建netty连接,执行远程调用 </p >
 *
 * @author baixiong
 * @version $Id: Client.java, v 0.1 2018年10月15日 11:28:00 baixiong Exp$
 */
public class Client {

    public static void main(String[] args) throws InterruptedException {
        RPCConsumer consumer = new RPCConsumer();
        int i = 0;
        while (true) {
            consumer.call(ConsumerConstant.APP_CODE, "aaa" + i++);
            Thread.sleep(2000L);
        }
    }
}
