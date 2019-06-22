package org.white.wrpc.common;

/**
 * <p> </p >
 *
 * @author white
 * @version $Id: Caller.java, v 0.1 2019年06月22日 上午16:24:00 white Exp$
 */
public interface Caller {

    /**
     * 调用
     * @param serverHost
     * @param param
     * @return
     */
    String call(String serverHost, String param) ;
}
