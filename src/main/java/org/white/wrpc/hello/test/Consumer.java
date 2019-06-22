package org.white.wrpc.hello.test;

import com.alibaba.fastjson.JSON;
import org.white.wrpc.consumer.RPCConsumer;
import org.white.wrpc.consumer.constant.ConsumerProperties;
import org.white.wrpc.hello.model.request.HelloRequest;
import org.white.wrpc.hello.model.response.HelloResponse;
import org.white.wrpc.hello.service.HelloService;

import java.util.Scanner;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: Consumer.java, v 0.1 2018年10月16日 14:56:00 white Exp$
 */
public class Consumer {

    public static void main(String[] args) throws InterruptedException {
        RPCConsumer consumer = new RPCConsumer();
        HelloService helloService = consumer.getBean(HelloService.class, ConsumerProperties.SERVER_APP_CODE);
        Scanner scan = new Scanner(System.in);
        while (true) {
            int seq = scan.nextInt();
            HelloRequest request = new HelloRequest();
            request.setSeq(seq);
            HelloResponse helloResponse = helloService.hello(request);
            System.out.println("客户端收到响应:" + JSON.toJSONString(helloResponse));
        }
    }
}
