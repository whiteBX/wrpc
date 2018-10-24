package org.white.wrpc.consumer.balance;

import org.white.wrpc.consumer.enums.BalanceMode;

import java.util.List;

/**
 * <p> </p >
 *
 * @author white
 * @version $Id: BalanceContext.java, v 0.1 2018年10月24日 上午22:37:00 white Exp$
 */
public class BalanceContext {

    private BalanceProcessor roundRobinProcessor = new RoundRobinBalanceProcessor();
    private BalanceProcessor randomProcessor = new RandomProcessor();

    /**
     * 获取URL
     *
     * @param urlList
     * @return
     */
    public String getUrl(String appCode, List<String> urlList, BalanceMode balanceMode) {
        switch (balanceMode) {
            case ROUND_ROBIN:
                return roundRobinProcessor.process(appCode, urlList);
            case RANDOM:
                return randomProcessor.process(appCode, urlList);
            default:
                return null;
        }
    }
}
