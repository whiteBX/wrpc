package org.white.wrpc.hello.service;

import org.white.wrpc.hello.model.request.HelloRequest;
import org.white.wrpc.hello.model.response.HelloResponse;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: HelloService.java, v 0.1 2018年10月16日 14:19:00 white Exp$
 */
public interface HelloService {

    HelloResponse hello(HelloRequest request);
}
