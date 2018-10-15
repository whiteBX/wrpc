package org.white.wrpc.common.zk;

import org.apache.zookeeper.*;
import org.white.wrpc.common.constant.CommonConstant;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;

/**
 * <p></p >
 *
 * @author white
 * @version $Id: ZKClient.java, v 0.1 2018年10月15日 09:50:00 white Exp$
 */
public class ZKClient {

    /**
     * 获取zookeeper连接
     *
     * @param connectString
     * @param sessionTimeout
     * @return
     */
    public ZooKeeper newConnection(String connectString, int sessionTimeout) {
        ZooKeeper zooKeeper = null;
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                    if (Event.KeeperState.SyncConnected.equals(watchedEvent.getState())) {
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
        } catch (IOException e) {
            System.out.println("获取zookeeper连接失败:连接不上zookeeper" + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("获取zookeeper连接失败:本地线程原因" + e.getMessage());
        }
        System.out.println("zookeeper连接成功");
        return zooKeeper;
    }

    /**
     * 创建临时节点
     *
     * @param zk
     * @param appCode
     * @param data
     */
    public void createEphemeralNode(ZooKeeper zk, String appCode, byte[] data) {
        try {
            initAppPath(zk, appCode);
            String path = zk.create(MessageFormat.format("{0}/{1}/", CommonConstant.ZK_REGISTORY_ROOT_PATH, appCode), data,
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println("临时节点创建成功：" + path);
        } catch (Exception e) {
            System.out.println("创建临时节点失败:" + e.getMessage());
        }
    }

    /**
     * 初始化appPath
     *
     * @param zk
     * @param appCode
     */
    private void initAppPath(ZooKeeper zk, String appCode) {
        initRootPath(zk);
        try {
            if (zk.exists(MessageFormat.format("{0}/{1}", CommonConstant.ZK_REGISTORY_ROOT_PATH, appCode), false) == null) {
                zk.create(MessageFormat.format("{0}/{1}", CommonConstant.ZK_REGISTORY_ROOT_PATH, appCode), null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            System.out.println("zookeeper创建跟节点失败" + e);
        }
    }

    /**
     * 初始化根节点
     *
     * @param zk
     */
    private void initRootPath(ZooKeeper zk) {
        try {
            if (zk.exists(CommonConstant.ZK_REGISTORY_ROOT_PATH, false) == null) {
                zk.create(CommonConstant.ZK_REGISTORY_ROOT_PATH, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            System.out.println("zookeeper创建跟节点失败" + e);
        }
    }

}
