package org.white.wrpc.provider;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: Server.java, v 0.1 2018年10月15日 09:48:00 white Exp$
 */
public class Server {

    public static void main(String[] args) throws InterruptedException {
        RPCProvider provider = new RPCProvider();
        provider.registry("127.0.0.1", 8091);
        provider.registry("127.0.0.1", 8092);
        provider.registry("127.0.0.1", 8093);
        provider.registry("127.0.0.1", 8094);
        provider.registry("127.0.0.1", 8095);

        Thread.sleep(Long.MAX_VALUE);
    }
}
