package org.white.wrpc.common.utils;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p> 生成全局唯一短码</p >
 *
 * @author white
 * @version $Id: ShortUUIDUtils.java, v 0.1 2018年10月27日 上午10:02:00 white Exp$
 */
public class ShortUUIDUtils {

    private static final int LOW_ORDER_THREE_BYTES = 0x00ffffff;
    private static final short PROC = NetUtil.getProcId();
    private static final int HOST = NetUtil.getMachineId();
    private static final AtomicInteger INC = new AtomicInteger(new SecureRandom().nextInt());
    private static final char[] HEX_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /***
     * 产生固定格式的UUID:[4字节Unix时间戳][3字节机器hash][2字节进程号][3字节自增].toHexString(24)
     *
     * @return UUID
     */
    public static String nextId() {

        int hid = HOST;
        short pid = PROC;
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        int counter = INC.getAndIncrement() & LOW_ORDER_THREE_BYTES;

        byte[] bytes = new byte[12];

        //encode timestamp
        bytes[0] = int3(timestamp);
        bytes[1] = int2(timestamp);
        bytes[2] = int1(timestamp);
        bytes[3] = int0(timestamp);

        //encode hostid
        bytes[4] = int2(hid);
        bytes[5] = int1(hid);
        bytes[6] = int0(hid);

        //encode pid
        bytes[7] = short1(pid);
        bytes[8] = short0(pid);

        //encode ID
        bytes[9] = int2(counter);
        bytes[10] = int1(counter);
        bytes[11] = int0(counter);

        return toHexString(bytes);
    }

    public static String toHexString(byte[] bs) {
        int i = 0;
        char[] chars = new char[24];
        for (byte b : bs) {
            chars[i++] = HEX_CHARS[b >> 4 & 0xF];
            chars[i++] = HEX_CHARS[b & 0xF];
        }
        return new String(chars);
    }

    private static byte int3(final int x) {
        return (byte) (x >> 24);
    }

    private static byte int2(final int x) {
        return (byte) (x >> 16);
    }

    private static byte int1(final int x) {
        return (byte) (x >> 8);
    }

    private static byte int0(final int x) {
        return (byte) (x);
    }

    private static byte short1(final short x) {
        return (byte) (x >> 8);
    }

    private static byte short0(final short x) {
        return (byte) (x);
    }
}
