package org.white.wrpc.consumer.balance;

import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.white.wrpc.common.constant.CommonConstant;
import org.white.wrpc.common.zk.ZKClient;
import org.white.wrpc.consumer.constant.ConsumerConstant;
import org.white.wrpc.consumer.enums.BalanceMode;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: UrlHolder.java, v 0.1 2018年10月15日 11:56:00 white Exp$
 */
public class UrlHolder {
    /**
     * url列表
     */
    private List<String> urlList = new ArrayList<String>();
    /**
     * zk客户端
     */
    private ZKClient zkClient = new ZKClient();
    /**
     * 负载上下文处理器
     */
    private BalanceContext balanceContext = new BalanceContext();
    /** 负载模式，后续引入spring则从配置文件中读取 */
    private static String balanceMode = "RANDOM";

    /**
     * 获取URL
     *
     * @param appCode
     * @return
     */
    public String getUrl(String appCode) {
        // 初始化url
        if (urlList.size() == 0) {
            initUrlList(appCode);
        }
        // 随机返回一条,此处以后优化为负载均衡策略
        if (urlList.size() > 0) {
            return balanceContext.getUrl(appCode, urlList, BalanceMode.valueOf(balanceMode));
        } else {
            System.out.println("目前没有服务提供者");
            return null;
        }
    }

    /**
     * 初始化urlList
     *
     * @param appCode
     */
    private void initUrlList(final String appCode) {
        try {
            // 获取zookeeper连接
            ZooKeeper zk = zkClient.newConnection(ConsumerConstant.ZK_CONNECTION_STRING,
                    ConsumerConstant.ZK_SESSION_TIME_OUT);
            // 获取目录下所有子节点
            String appPath = MessageFormat.format("{0}/{1}", CommonConstant.ZK_REGISTORY_ROOT_PATH, appCode);
            List<String> urlNodeList = zk.getChildren(appPath
                    , new Watcher() {
                        public void process(WatchedEvent watchedEvent) {
                            initUrlList(appCode);
                        }
                    });
            if (CollectionUtils.isEmpty(urlNodeList)) {
                return;
            }
            // 从子节点数据中解析出所有url
            List<String> urlList = new ArrayList<String>();
            for (String path : urlNodeList) {
                byte[] url = zk.getData(appPath + "/" + path, new Watcher() {
                    public void process(WatchedEvent watchedEvent) {
                        initUrlList(appCode);
                    }
                }, null);
                if (url != null) {
                    urlList.add(new String(url));
                }
            }
            this.urlList = urlList;
        } catch (Exception e) {
            System.out.println("初始化url异常" + e.getMessage());
        }
    }

}
