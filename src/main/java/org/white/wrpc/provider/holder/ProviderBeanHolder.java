package org.white.wrpc.provider.holder;

import java.util.HashMap;
import java.util.Map;

/**
 * <p></p >
 *
 * @author baixiong
 * @version $Id: ProviderBeanHolder.java, v 0.1 2018年10月16日 15:39:00 baixiong Exp$
 */
public class ProviderBeanHolder {

    /**
     * bean注册缓存
     */
    private static Map<String, Object> providerList = new HashMap<String, Object>();

    /**
     * 注册
     * @param clazzName
     * @param obj
     */
    public static void regist(String clazzName, Object obj) {
        providerList.put(clazzName, obj);
    }

    /**
     * 获取
     * @param clazzName
     * @return
     */
    public static Object getBean(String clazzName) {
        return providerList.get(clazzName);
    }
}
