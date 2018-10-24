package org.white.wrpc.consumer.balance;

import java.util.List;

/**
 * <p> </p >
 *
 * @author white
 * @version $Id: RandomProcessor.java, v 0.1 2018年10月24日 上午22:45:00 white Exp$
 */
public class RandomProcessor implements BalanceProcessor {

    public String process(String appCode, List<String> urlList) {
        return urlList.get((int) (Math.random() * urlList.size()));
    }
}
