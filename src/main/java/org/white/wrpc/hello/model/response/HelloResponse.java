package org.white.wrpc.hello.model.response;

/**
 * <p></p >
 *
 * @author baixiong
 * @version $Id: HelloResponse.java, v 0.1 2018年10月16日 14:59:00 baixiong Exp$
 */
public class HelloResponse {

    private int code;

    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
