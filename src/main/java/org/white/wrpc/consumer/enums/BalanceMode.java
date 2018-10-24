package org.white.wrpc.consumer.enums;

/**
 * <p> </p >
 *
 * @author white
 * @version $Id: BalanceMode.java, v 0.1 2018年10月24日 上午22:39:00 white Exp$
 */
public enum BalanceMode {

    ROUND_ROBIN("轮询"),
    RANDOM("随机");

    private String desc;

    BalanceMode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
