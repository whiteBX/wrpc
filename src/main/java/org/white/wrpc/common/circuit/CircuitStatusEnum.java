package org.white.wrpc.common.circuit;

/**
 * <p>熔断状态枚举</p >
 *
 * @author white
 * @version $Id: CircuitStatusEnum.java, v 0.1 2019年05月14日 14:30:00 white Exp$
 */
public enum CircuitStatusEnum {

    OPEN("OPEN", "打开"),
    HALF_OPEN("HALF_OPEN", "半开"),
    CLOSE("CLOSE", "关闭");

    /**
     * code
     */
    private String code;
    /**
     * 描述
     */
    private String desc;

    CircuitStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
