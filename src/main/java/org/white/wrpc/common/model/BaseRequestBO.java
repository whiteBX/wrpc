package org.white.wrpc.common.model;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: BaseRequestBO.java, v 0.1 2018年10月25日 16:12:00 white Exp$
 */
public class BaseRequestBO {
    /**
     * 链路追踪span
     */
    private Span   span;
    /**
     * 类名
     */
    private String clazzName;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 方法参数类型
     */
    private String paramTypeName;
    /**
     * 数据
     */
    private String data;

    public Span getSpan() {
        return span;
    }

    public void setSpan(Span span) {
        this.span = span;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParamTypeName() {
        return paramTypeName;
    }

    public void setParamTypeName(String paramTypeName) {
        this.paramTypeName = paramTypeName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
