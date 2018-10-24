package org.white.wrpc.consumer.balance;

import java.util.List;

/**
 * <p> </p >
 *
 * @author white
 * @version $Id: BalanceProcessor.java, v 0.1 2018年10月24日 上午22:25:00 white Exp$
 */
public interface BalanceProcessor {

    /**
     * 处理接口
     * @param urlList
     * @return
     */
    String process(String appCode, List<String> urlList);
}
