package org.white.wrpc.common.model;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: BaseResponseBO.java, v 0.1 2018年10月25日 17:52:00 white Exp$
 */
public class BaseResponseBO {
    /**
     * 链路追踪span
     */
    private Span   span;
    /**
     * 响应
     */
    private String response;

    public Span getSpan() {
        return span;
    }

    public void setSpan(Span span) {
        this.span = span;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
