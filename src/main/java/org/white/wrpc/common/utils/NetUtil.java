/*
 * GuoXiaoMei.com Inc.
 * Copyright (c) 2017-2018 All Rights Reserved.
 */
package org.white.wrpc.common.utils;

import java.lang.management.ManagementFactory;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Enumeration;

/**
 * 网络工具类
 *
 * @author white
 * @version $Id: ShortUUIDUtils.java, v 0.1 2018年10月27日 上午10:02:00 white Exp$
 */
public class NetUtil {

    private static String HOST;
    private static short PROC_ID;
    private static int HOST_NUM;
    private static int MACHINEPIECE;
    private static final int LOW_ORDER_THREE_BYTES = 0x00ffffff;

    static {
        HOST = "127.0.0.1";
        try {
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                sb.append(iface.toString());

                byte[] mac = iface.getHardwareAddress();
                if (mac != null) {
                    ByteBuffer bb = ByteBuffer.wrap(mac);
                    try {
                        sb.append(bb.getChar());
                        sb.append(bb.getChar());
                        sb.append(bb.getChar());
                    } catch (BufferUnderflowException shortHardwareAddressException) {
                        //NOPMD mac with less than 6 bytes. continue
                    }
                }

                if (iface.isLoopback() || !iface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet6Address) {
                        continue;
                    }
                    HOST = addr.getHostAddress();
                    HOST_NUM = ByteBuffer.wrap(addr.getAddress()).getInt();
                }
            }

            try {
                String name = ManagementFactory.getRuntimeMXBean().getName();
                int index = name.indexOf("@");
                if (index != -1) {
                    PROC_ID = (short) Integer.parseInt(name.substring(0, index));
                } else {
                    PROC_ID = (short) name.hashCode();
                }
            } catch (Exception e) {
                System.out.println("fail to get proc id,will create random " + e);
                PROC_ID = (short) (new SecureRandom().nextInt());
            }

            MACHINEPIECE = sb.toString().hashCode();
        } catch (Exception e) {
            // exception sometimes happens with IBM JVM, use random
            MACHINEPIECE = (new SecureRandom().nextInt());
            throw new RuntimeException(e);
        }
        // build a 3-byte machine piece based on NICs info
        MACHINEPIECE = MACHINEPIECE & LOW_ORDER_THREE_BYTES;
    }

    public static int getMachineId() {
        return MACHINEPIECE;
    }

    public static String getHost() {
        return HOST;
    }

    public static int getHostNum() {
        return HOST_NUM;
    }

    public static short getProcId() {
        return PROC_ID;
    }

    public static String toHost(int ip) {
        return new StringBuilder().append(((ip >> 24) & 0xff)).append(".").append((ip >> 16) & 0xff).append(".")
                .append((ip >> 8) & 0xff).append(".").append((ip & 0xff)).toString();
    }

    public static int random(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

}
