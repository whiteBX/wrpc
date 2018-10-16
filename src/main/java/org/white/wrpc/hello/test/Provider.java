package org.white.wrpc.hello.test;

import org.white.wrpc.hello.service.HelloService;
import org.white.wrpc.hello.service.impl.HelloServiceImpl;
import org.white.wrpc.provider.RPCProvider;

/**
 * <p></p >
 *
 * @author baixiong
 * @version $Id: Provider.java, v 0.1 2018年10月16日 14:56:00 baixiong Exp$
 */
public class Provider {

    public static void main(String[] args) throws InterruptedException {
        RPCProvider provider = new RPCProvider();
        provider.registry("127.0.0.1", 8091);
        provider.registry("127.0.0.1", 8092);
        provider.registry("127.0.0.1", 8093);
        provider.registry("127.0.0.1", 8094);
        provider.registry("127.0.0.1", 8095);
        provider.provide(HelloService.class, new HelloServiceImpl());

        Thread.sleep(Long.MAX_VALUE);
    }
}
