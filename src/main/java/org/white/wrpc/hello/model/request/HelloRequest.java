package org.white.wrpc.hello.model.request;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: HelloRequest.java, v 0.1 2018年10月16日 14:59:00 white Exp$
 */
public class HelloRequest {

    private int    seq;

    private String content;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
